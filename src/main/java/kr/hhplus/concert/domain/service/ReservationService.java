package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation makeReservation(Long userId, Integer concertScheduleId, Integer seatId) {
        Reservation reservation = Reservation.create(userId, concertScheduleId, seatId);
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Integer reservationId) {
        return reservationRepository.findById(reservationId);
    }

}
