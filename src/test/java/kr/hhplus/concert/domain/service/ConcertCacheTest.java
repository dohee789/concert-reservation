package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.TestcontainersConfiguration;
import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Sql("/init.sql")
class ConcertCacheTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertScheduleService concertScheduleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void canSelectCachedConcert() {
        // given
        Long concertId = 1L;
        String cacheKey = "concert::" + concertId;

        // when
        // 1차 조회 (DB 접근)
        Concert concert = concertService.findConcertById(concertId);
        // 2차 조회 (캐시 사용)
        Concert cachedConcert = concertService.findConcertById(concertId);

        // then
        assertNotNull(concert);
        assertNotNull(cachedConcert);
        System.out.println("Redis 키 :" + cacheKey);
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        assertTrue(hasKey);
    }

    @Test
    void canSelectCachedConcertSchedule() {
        // given
        Long concertId = 1L;
        LocalDateTime scheduleDate = LocalDateTime.of(2025, 5, 5, 20, 0,0);
        String cacheKey = "concertSchedule::" + concertId + ":" + scheduleDate.toLocalDate();

        // when
        // 1차 조회 (DB 접근)
        ConcertSchedule schedule = concertScheduleService.findConcertSchedulesByIdAndDate(concertId, scheduleDate);
        // 2차 조회 (캐시 사용)
        ConcertSchedule cachedSchedule = concertScheduleService.findConcertSchedulesByIdAndDate(concertId, scheduleDate);

        // then
        assertNotNull(schedule);
        assertNotNull(cachedSchedule);
        System.out.println("Redis 키 : " + cacheKey);
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        assertTrue(hasKey);
    }

    @Test
    void performanceConcertCache() {
        // given
        Long concertId = 1L;
        int iterations = 100;
        long dbAccessTime = 0;
        long cacheAccessTime = 0;

        // when
        // DB 접근 시간 측정 (캐시를 비워가며 반복 테스트)
        for (int i = 0; i < iterations; i++) {
            // 매 반복마다 캐시 초기화
            redisTemplate.getConnectionFactory().getConnection().flushAll();

            long startTime = System.nanoTime();
            concertService.findConcertById(concertId);
            long endTime = System.nanoTime();

            dbAccessTime += (endTime - startTime);
        }

        // when
        // cache 접근 시간 측정
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            concertService.findConcertById(concertId);
            long endTime = System.nanoTime();

            cacheAccessTime += (endTime - startTime);
        }

        // then
        double dbAvgTime = dbAccessTime / (double) iterations / 1_000_000.0;
        double cacheAvgTime = cacheAccessTime / (double) iterations / 1_000_000.0;
        double speedup = dbAvgTime / cacheAvgTime;

        System.out.println("===== Concert 캐시 성능 테스트 결과 =====");
        System.out.println("DB 접근 평균 시간: " + dbAvgTime + " ms");
        System.out.println("캐시 접근 평균 시간: " + cacheAvgTime + " ms");
        System.out.println("성능 향상 비율: " + speedup + "배");

        // then
        assertTrue(cacheAvgTime < dbAvgTime);
    }

    @Test
    void multiThreadCaching_PerformanceConcertCache() throws InterruptedException {
        // given
        Long concertId = 1L;
        int threadCount = 20;
        int requestsPerThread = 50;
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicLong totalTimeWithoutCache = new AtomicLong(0);
        AtomicLong totalTimeWithCache = new AtomicLong(0);

        // when (캐시 없이 조회)
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        List<Thread> threadsWithoutCache = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(() -> {
                try {
                    long threadTime = 0;
                    for (int j = 0; j < requestsPerThread; j++) {
                        redisTemplate.getConnectionFactory().getConnection().flushAll();
                        long startTime = System.nanoTime();
                        concertService.findConcertById(concertId);
                        long endTime = System.nanoTime();
                        threadTime += (endTime - startTime);
                    }
                    totalTimeWithoutCache.addAndGet(threadTime);
                } finally {
                    latch.countDown();
                }
            });
            threadsWithoutCache.add(t);
            t.start();
        }

        latch.await();

        // when (캐시 사용 조회)
        CountDownLatch latch2 = new CountDownLatch(threadCount);

        List<Thread> threadsWithCache = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(() -> {
                try {
                    long threadTime = 0;
                    for (int j = 0; j < requestsPerThread; j++) {
                        long startTime = System.nanoTime();
                        concertService.findConcertById(concertId);
                        long endTime = System.nanoTime();
                        threadTime += (endTime - startTime);
                    }
                    totalTimeWithCache.addAndGet(threadTime);
                } finally {
                    latch2.countDown();
                }
            });
            threadsWithCache.add(t);
            t.start();
        }

        latch2.await();

        // then
        double avgTimeWithoutCache = totalTimeWithoutCache.get() / (double) (threadCount * requestsPerThread) / 1_000_000.0;
        double avgTimeWithCache = totalTimeWithCache.get() / (double) (threadCount * requestsPerThread) / 1_000_000.0;
        double speedup = avgTimeWithoutCache / avgTimeWithCache;

        System.out.println("===== 캐시 성능 테스트 결과 =====");
        System.out.println("스레드 수: " + threadCount + ", 스레드당 요청 수: " + requestsPerThread);
        System.out.println("캐시 없이 평균 시간: " + avgTimeWithoutCache + " ms");
        System.out.println("캐시 사용 평균 시간: " + avgTimeWithCache + " ms");
        System.out.println("성능 향상 비율: " + speedup + "배");

        // then
        assertTrue(avgTimeWithCache < avgTimeWithoutCache);
    }
}

