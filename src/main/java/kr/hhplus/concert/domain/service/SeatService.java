package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
import kr.hhplus.concert.domain.repository.SeatRepository;

public class SeatService {
    private SeatRepository seatRepository;

    public Seat findSeat(Integer concertScheduleId, Integer seatId) {
        Seat seat = seatRepository.findSeatById(concertScheduleId, seatId);
        if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
            throw new SeatUnavailableException(seat.getSeatStatus());
        }
        return seat;
    }

    public void assignSeat(Integer concertScheduleId, Integer seatId){
        Seat seat = findSeat(concertScheduleId, seatId);
        seat.isReserved();
    }
}
