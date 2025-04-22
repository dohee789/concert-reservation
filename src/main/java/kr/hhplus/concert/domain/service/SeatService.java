package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
import kr.hhplus.concert.domain.repository.SeatRepository;

import java.util.NoSuchElementException;

public class SeatService {
    private SeatRepository seatRepository;

    public Seat findSeat(Long concertScheduleId) {
        return seatRepository.findById(concertScheduleId)
                .orElseThrow(() -> new SeatUnavailableException(seat.getSeatStatus()));
    }

    public void assignSeat(Long concertScheduleId){
        Seat seat = findSeat(concertScheduleId);
        seat.reserve();
    }
}
