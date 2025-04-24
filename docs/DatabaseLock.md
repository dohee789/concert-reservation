# Database

Author: dohee

# ‘콘서트 예약 서비스’ 시나리오 기반

### 🔁 개요

- **시스템 흐름 요약**
    - 유저는 **토큰 발급**을 통해 활성열 진입 (활성 큐 최대 크기 존재)
    - **5분 내 좌석 선택 및 결제**를 완료해야 하며, **잔액 부족 시 충전 필요**
    - **스케줄러**는 토큰 만료 시 대기열 진입 및 좌석 점유 상태 해제 (**보정 용도**로, 실시간 동시성은 API 단에서 해결)
    - **좌석 선택/결제 시점에 토큰 만료 여부**를 실시간으로 확인

---

## 조회 성능 향상 + DB 부하 감소를 위한 인덱싱 적용

### 🧬 인덱스 구성

### 📁 `queue`

| 컬럼 | 인덱스 |
| --- | --- |
| `user_id` | UNIQUE INDEX (`user_id`) - 1인 1큐 제한 |
| `token` | UNIQUE INDEX (`token`) - 토큰 기반 상태 변경 및 검증에 사용 |
| `queue_status` | INDEX (`queue_status`) - 활성/대기열 인입 유저 조회 |

---

### 📁 `concert_schedule`

| 컬럼 | 인덱스 |
| --- | --- |
| `concert_id` | INDEX - 공연별 스케줄 조회 |
| `schedule_date_time` | 복합 유니크 INDEX 
(`concert_id`,`schedule_date_time`) - 스케줄 시간순 정렬 |

---

### 📁 `seat`

| 컬럼 | 인덱스 |
| --- | --- |
| `concert_schedule_id` | INDEX - 공연스케줄별 좌석 조회 |
| `seat_number` | 복합 유니크 INDEX (`concert_schedule_id`, `seat_number`) - 공연장 내 좌석 구분 |
| `seat_status` | INDEX - 좌석 상태 필터링 |

---

### 📁 `wallet`

| 컬럼 | 인덱스 |
| --- | --- |
| `user_id` | UNIQUE INDEX - 유저별 결제 정보 조회 |
| `processed_at` | INDEX - 거래 내역 정렬 |

---

### 📁 `reservation`

| 컬럼 | 인덱스 |
| --- | --- |
| `user_id` | INDEX - 특정 유저 예약 조회 |
| `seat_id` | 복합 UNIQUE (`user_id`, `seat_id`) - 좌석 중복 예약 방지 |
| `reserved_at` | INDEX - 예약 내역 시간 순 정렬  |

---

## 동시성 이슈 개선을 위한 DB Lock 적용 전략

---

### ✅ 문제 식별

| 이슈 구간 | 발생 가능 이슈 | AS-IS |
| --- | --- | --- |
| 좌석 예약 | 중복 예약 | 동일 좌석에 대해 두 명의 유저가 동시에 예약 요청 시 동시성 문제로 둘 다 성공할 수 있음 |
| 결제  | 초과 결제 | 동일 사용자가 잔액을 초과하는 결제 요청을 동시에 보낼 경우, 중복 차감이 될 수 있음 |
| 큐 진입 및 토큰 처리 | 진입 조건 레이스 | 활성열 인원 초과 시 토큰 발급 레이스 조건 발생 가능성 |
| 토큰 만료 시 상태 정리 | 좌석 회수 실패 | 스케줄러 미동작 또는 지연 시, 좌석이 점유 상태로 남을 수 있음 |

---

### 🔧 해결 방안

- 낙관적 락 (**optimistic lock**) ?
    - DB의 Lock을 사용하지 않고 Version 관리를 통해 Application Level에서 처리하는 논리적인 락
- 비관적 락 (**pessimistic lock**) ?
    - DB의 Lock을 (공유, 베타 락) 사용하여 처리하는 물리적인 락

| 구간 | 해결 방안 | TO-BE  |
| --- | --- | --- |
| 좌석 선점 | 낙관적 Lock | `@Version`  |
| 결제 및 충전 | 비관적 Lock | `@Lock(LockModeType.PESSIMISTIC_WRITE)` |

### 좌석 선점

> 좌석 선점시 복수 사용자의 동시 요청으로 인한 중복 선점을 제어하기 위해 `@Version`을 사용하여 변경 시점에 버전 충돌 여부를 감지하고 충돌시에 예외처리를 하였다.
> 
1. version 이 포함된 좌석 정보 조회
2. 예약 시도시 version 값이 같다면 업데이트 실패
3. 예외 발생
4. 트랜잭션 롤백

---

### 결제 및 충전

> 결제 시 사용자의 잔액 차감 로직에서 동시 결제 요청을 제어하기 위해 `PESSIMISTIC_WRITE` 락을 도입하였다. 트랜잭션 경계를 "파사드 단위"에서 설정하여, 이 파사드 메서드가 호출될 경우 모든 작업이 완전히 성공하거나 실패하도록 보장하였다.
> 
1. 락을 걸고 조회
2. 결제 시도 
3. 결제 및 결제이력  저장
4. 트랜잭션 커밋

---

### 🚨 테스트

테스트컨테이너를 통해 테스트를 위한 데이터를 `@Sql("/init.sql")` 으로 세팅하여 진행하였다.

> 좌석 선점
> 

```java
void seatReservationConcurrencyControl_With_OptimisticLock() throws InterruptedException {
        int threadCount = 5;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        for(int i=0; i< threadCount;i++) {
            int userId = i;
            service.submit(() -> {
                ReservationCommand command = ReservationCommand.builder()
                        .userId((long)userId)
                        .concertId(10L)
                        .concertScheduleId(1L)
                        .seatId(1L)
                        .build();
                try {
                    ReservationResult result = reservationFacade.makeReservation(command);
                    System.out.println("Result for user " + userId + ": " + result);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Exception for user " + userId + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        service.shutdown();
        assertEquals(1, successCount.get());
    }
```

잔액 선점 동시성 테스트를 통해 낙관적락 (`@Version`) 적용시 다수의 유저가 동일한 좌석을 점유하는 경우 활성열에 존재하는 가장 빠른 유저만 점유에 성공하고 미적용시 예외처리가 되는 테스트를 진행하였다.

> 결제 및 충전
> 

```java
void paymentConcurrencyControl_With_PessimisticLock() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    walletFacade.wallet(1L);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Wallet wallet = walletRepository.findByUserId(1L).orElseThrow();
        System.out.println("In user wallet = " + wallet.getBalance());
    }
```

결제 동시성 테스트를 통해 비관적락(`@Lock(LockModeType.PESSIMISTIC_WRITE)`) 적용시 동일한 유저의 잔액이 한번만 차감되고 미적용시 예외처리가 되는 테스트를 진행하였다.