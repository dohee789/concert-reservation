package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.TestcontainersConfiguration;
import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.application.facade.ReservationFacade;
import kr.hhplus.concert.application.facade.WalletFacade;
import kr.hhplus.concert.domain.model.reservation.Reservation;
import kr.hhplus.concert.domain.model.wallet.Wallet;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Sql("/init.sql")
public class ReservationCacheTest {
    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    WalletFacade walletFacade;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void makeReservation_And_PayByCachedReservation() {
        // given
        Long userId = 2L;
        Long concertScheduleId = 2L;
        Long seatNumber = 2L;
        ReservationCommand command = new ReservationCommand(userId, concertScheduleId, seatNumber);

        // when
        ReservationResult result = reservationFacade.makeReservation(command);
        Long reservationId = result.id();
        // then
        String cacheKeyPrefix = "reservation::";
        String compositeKey = userId + ":" + concertScheduleId + ":" + seatNumber;
        System.out.println("reservation key: " + cacheKeyPrefix + compositeKey);
        Object cachedReservation = redisTemplate.opsForValue().get(cacheKeyPrefix + compositeKey);
        assertThat(cachedReservation).isNotNull();
        assertThat(((Reservation) cachedReservation).getId()).isEqualTo(reservationId);

        // when
        Wallet wallet = walletFacade.wallet(userId, reservationId);
        // then
        assertThat(wallet).isNotNull();
        assertThat(wallet.getUserId()).isEqualTo(userId);
    }

}
