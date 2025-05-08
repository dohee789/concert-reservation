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
}

