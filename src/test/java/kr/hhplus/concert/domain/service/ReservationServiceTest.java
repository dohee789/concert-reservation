package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @DisplayName("콘서트 예약을 생성할 수 있다")
    @Test
    void makeConcertReservation_success() {
        // given
        Long userId = 1L;
        Integer scheduleId = 10;
        Integer seatId = 100;
        Reservation reservation = Reservation.create(userId, scheduleId, seatId);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);
        // when
        Reservation result = reservationService.makeReservation(userId, scheduleId, seatId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getConcertScheduleId()).isEqualTo(scheduleId);
        assertThat(result.getSeatId()).isEqualTo(seatId);
        Mockito.verify(reservationRepository).save(Mockito.any(Reservation.class));
    }

    @DisplayName("예약한 콘서트 내역을 조회할 수 있다")
    @Test
    void getConcertReservation_success() {
        // given
        Integer reservationId = 1;
        Reservation reservation = Reservation.create(1L, 10, 100);
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(reservation);

        Reservation result = reservationService.getReservation(reservationId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getConcertScheduleId()).isEqualTo(10);
        Mockito.verify(reservationRepository).findById(reservationId);
    }



}