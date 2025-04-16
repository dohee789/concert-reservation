package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Seat;

public interface SeatRepository {
    Seat findSeatById(Long concertId, Long seatId);
}
