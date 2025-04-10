package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Reservation findById(Integer reservationId);
}
