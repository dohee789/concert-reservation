package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.model.reservation.Reservation;
import kr.hhplus.concert.domain.model.seat.Seat;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @CachePut(value = "reservation", key = "#queue.userId + ':' + #seat.concertSchedule.id + ':' + #seat.seatNumber")
    public Reservation makeReservation(Queue queue, Seat seat) {
        try {
            Reservation reservation = Reservation.create(queue, seat);
            queue.expireToken();
            return reservationRepository.save(reservation);
        } catch (IllegalStateException e){
            seat.notAssigned();
            throw new SeatUnavailableException(seat.getSeatStatus());
        }
    }

    @Cacheable(value = "reservation", key = "#reservationId", unless = "#result == null")
    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
    }
}
