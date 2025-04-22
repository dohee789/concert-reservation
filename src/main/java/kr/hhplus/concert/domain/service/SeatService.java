package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
import kr.hhplus.concert.domain.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public Seat findSeat(Long concertScheduleId) {
        return seatRepository.findById(concertScheduleId)
                .orElseThrow(() -> new NoSuchElementException("유효한 AVAILABLE 상태의 좌석이 없습니다."));
    }

    @Transactional
    public void assignSeat(Long concertScheduleId){
        Seat seat = findSeat(concertScheduleId);
        seat.reserve();
        seatRepository.save(seat);
    }
}
