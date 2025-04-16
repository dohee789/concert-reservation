package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation makeReservation(Queue queue, Seat seat) {
        Reservation reservation = Reservation.create(queue, seat);
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

}
