package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.seat.Seat;
import kr.hhplus.concert.domain.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public Seat findSeat(Long concertScheduleId) {
        return seatRepository.findById(concertScheduleId)
                .orElseThrow(() -> new NoSuchElementException("유효한 상태의 좌석이 없습니다."));
    }

    @Transactional
    public void assignSeat(Long concertScheduleId){
        Seat seat = findSeat(concertScheduleId);
        seat.pending();
        seatRepository.save(seat);
    }

    public boolean isSoldOut(Long concertScheduleId) {
        Integer remaining = seatRepository.countAvailableSeatsByConcertScheduleId(concertScheduleId);
        return remaining == 0;
    }
}
