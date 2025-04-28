package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation makeReservation(Queue queue, Seat seat) {
        try {
            Reservation reservation = Reservation.create(queue, seat);
            return reservationRepository.save(reservation);
        } catch (IllegalStateException e){
            seat.withhold();
            throw new SeatUnavailableException(seat.getSeatStatus());
        }
    }

    public Reservation getReservation(Long userId) {
        return reservationRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
    }
}
