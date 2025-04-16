package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.*;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
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

import java.time.LocalDateTime;
import java.util.Optional;

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
        Integer seatId = 100;
        ConcertSchedule concertSchedule = new ConcertSchedule(userId, LocalDateTime.now(), Concert.builder().build());
        Seat seat = Seat.builder().seatNumber(seatId).concertSchedule(concertSchedule).seatStatus(SeatStatus.AVAILABLE).build();
        Queue queue = Queue.builder().userId(userId).queueStatus(QueueStatus.ACTIVE).build();

        Reservation reservation = Reservation.create(queue, seat);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation result = reservationService.makeReservation(queue, seat);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        Mockito.verify(reservationRepository).save(Mockito.any(Reservation.class));
    }

    @DisplayName("예약한 콘서트 내역을 조회할 수 있다")
    @Test
    void getConcertReservation_success() {
        // given
        Long reservationId = 1L;
        Reservation reservation = Reservation.create(Queue.builder().build(), Seat.builder().build());
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.ofNullable(reservation));

        Optional<Reservation> result = reservationService.getReservation(reservationId);
        // then
        assertThat(result).isNotNull();
        Mockito.verify(reservationRepository).findById(reservationId);
    }



}