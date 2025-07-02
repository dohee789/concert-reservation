# **‘콘서트 예약 서비스’ 시나리오 기반**

---

### 목적

- **사용자 예약 흐름의 병목 지점 및 한계 확인**
- **활성열/대기열 시스템에 따른 구조적 제약 검증**
- **예약 API의 TPS 대응력 및 안정성 확보**

---

## 🔧 시스템 개요 요약

| 항목 | 설명 |
| --- | --- |
| **토큰 발급** | 유저가 대기열에 진입하는 행위. 활성열에 여유가 있으면 곧바로 활성열 진입. |
| **활성열 제한** | 최대 50명만 동시에 존재 가능 |
| **예약 조건** | 활성열에 있을 때만 예약 가능 |

---

## 🎯 부하 테스트 대상 및 유형

| 테스트 대상 | 목적 | 적합한 테스트 유형 |
| --- | --- | --- |
| **토큰 발급 API** | 동시 사용자 유입 시 대기열/활성열 처리 성능 측정 | **Spike Test / Stress Test** |
| **활성열 → 예약 API** | 최대 50명 동시 예약 요청 처리 성능 측정 (연쇄 파사드 호출 포함) | **Load Test / Peak Test** |

---

## 📄 테스트 시나리오 요약

### ✅ 시나리오 1: 대규모 사용자 토큰 발급 (Spike Test)

- 300명의 사용자가 동시에 토큰 발급 요청
- 시스템은 최대 50명만 활성열로 진입
- 나머지는 대기열에 배치됨

```jsx

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '2s', target: 300 }, // Spike in 2 seconds
    { duration: '5s', target: 300 },
    { duration: '2s', target: 0 },
  ],
};

const BASE_URL = 'http://localhost:8080';
const API_ENDPOINT = '/api/v1/token/create';

export default function () {
  const res = http.post(BASE_URL + API_ENDPOINT, JSON.stringify({
    userId: `user_${__VU}`,
  }), { headers: { 'Content-Type': 'application/json' } });

  check(res, { 'token 발급 성공': (r) => r.status === 200 });
  sleep(1);
}

```

### 🎯 목적

- 활성열이 50명으로 제한되어 있는 상황에서, 대량 유입 시 대기열 시스템이 잘 작동하는지 확인

---

### ✅ 시나리오 2: 활성열 사용자 예약 처리 (Peak Test)

- 활성열 인원 50명이 동시에 예약 API 호출
- 예약 시 좌석 선점 → 결제

```jsx
import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 50,
  duration: '10s',
};

const BASE_URL = 'http://localhost:8080';
const API_ENDPOINT = '/api/v1/reservation';

export default function () {
  const res = http.post(BASE_URL + API_ENDPOINT, JSON.stringify({
    userId: `user_${__VU}`,
    concertId: 123,
    concertScheduleId: 456,
    seatId: 1
  }), { headers: { 'Content-Type': 'application/json' } });

  check(res, { '예약 성공': (r) => r.status === 200 });
}

```

### 🎯 목적

- 활성열 50명에 대해 예약 API의 동시 처리 성능을 확인 (TPS 최대치 확인용)

---

## 📊 지표 및 기대 결과

| 측정 항목 | 기대 수치 |
| --- | --- |
| Token API 응답 시간 | 300ms 이하 |
| 예약 API 성공률 | 99% 이상 (활성열 사용자 기준) |
| 활성열 유지 시간 | 5분 이내 자동 만료 |
| TPS 추이 | 초당 10~30 요청 평균 처리 |
| 실패율 / 에러율 | Spike 상황에서도 1% 이하 |

---

## 🚨 장애 시나리오 및 대응 방안

### ⚠️ 시나리오 1: 토큰 발급 폭주로 Redis 부하 증가 (Spike Test 중)

- **징후**
    - 토큰 발급 API 응답 시간 급증
    - Redis 연결 시간 초과 또는 `Too many connections` 오류
    - `500` 또는 `503` 응답 증가
- **원인**
    - Redis에 동시 ZADD 요청 과도 (대기열 + 활성열 상태 업데이트)
    - 토큰 발급 중 활성열 판단 로직 병목
- **즉각 대응**
    - Redis 모니터링
    - 활성열 처리 로직에서 락 분리 여부 확인 (분산 락 사용 지점 재확인)
    - 트래픽 조절
- **사후 조치**
    - Redis 커넥션 풀/스레드 수 증설
    - 활성열 입장 판단 로직 캐싱 구조로 변경
    - 임계치 초과 시 즉시 대기열로 우회하는 회피 로직 추가

---

### ⚠️ 시나리오 2: 활성열 50명 동시 예약 요청 시 DB 병목 (Peak Test 중)

- **징후**
    - 예약 API 응답 지연 (Latency > 1s)
    - DB CPU/Lock 경합 발생
    - Kafka 또는 Seat/Wallet 등 연계 도메인 timeout 발생
- **원인**
    - 50명 동시 요청 → 좌석 선점 & 결제 처리 로직 병목
    - 예약 트랜잭션 처리 중 DB Lock 과다 (Pessimistic Lock 등)
- **즉각 대응**
    - 활성열 사용자 수 임시 제한 (e.g. 30명으로 줄이기)
    - Kafka 처리 상태 모니터링
    - 해당 시간대 장애 사용자 리스트 기록 (보상용)
- **사후 조치**
    - 좌석 선점 처리에 대한 파티셔닝 키 개선
    - Kafka DLQ 활성화 및 재처리 전략 구성
    - DB 인덱싱 재설계 + 배치 예약 큐 분리 가능성 검토

---

## 🧰 공통 장애 대응 체크리스트

| 항목 | 점검 항목 |
| --- | --- |
| ✅ 로그 | API 실패율, 스택트레이스, 요청량 분석 |
| ✅ Redis | 커넥션 수, TTL 확인, 명령어 사용량 분석 |
| ✅ DB | Lock 대기 시간, 트랜잭션 수, 인덱스 상태 |
| ✅ Kafka | Consumer lag, DLQ 적재 여부 |
| ✅ 스케줄러 | Job 실행 여부, 예외 여부, 시간 간격 확인 |

---

## 📝 사후 리포트 작성 항목

1. 장애 시각 및 지속 시간
2. 발생 API 및 응답 상태 변화 그래프
3. Redis/DB 지표 변화 (CPU, 연결 수, TPS 등)
4. 사용자 영향도 (장애 인입 대상 수)
5. 조치 내용 및 근거
6. 재발 방지를 위한 개선 작업 정리