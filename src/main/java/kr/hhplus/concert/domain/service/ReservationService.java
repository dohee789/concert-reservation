package kr.hhplus.concert.domain.service;

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
        Reservation reservation = Reservation.create(queue, seat);
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long userId) {
        return reservationRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("예약 정보를 찾을 수 없습니다."));
    }
}
