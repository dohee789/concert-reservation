package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Reservation;

import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long userId);
}
