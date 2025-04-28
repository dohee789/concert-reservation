package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Seat;

import java.util.Optional;

public interface SeatRepository {
    Seat save(Seat seat);
    Optional<Seat> findById(Long concertScheduleId);
}
