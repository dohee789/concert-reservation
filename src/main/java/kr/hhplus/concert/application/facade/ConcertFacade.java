package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.ConcertScheduleService;
import kr.hhplus.concert.domain.service.ConcertService;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ConcertFacade {
    private QueueService queueService;
    private ConcertService concertService;
    private ConcertScheduleService concertScheduleService;
    private SeatService seatService;

    public ConcertSchedule findSchedules(Long userId, Long id, LocalDate scheduleDate) {
        // 토큰 검증
        queueService.validateToken(userId);
        // 콘서트 찾기
        Concert concert = concertService.findConcertById(id);
        return concertScheduleService.findConcertSchedulesByIdAndDate(concert.id(), scheduleDate);
    }

    public Seat findSeats(Long userId, Long id, LocalDate scheduleDate, Integer seatId) {
        ConcertSchedule concertSchedule = findSchedules(userId, id, scheduleDate);
        // 자리 찾기
        return seatService.findSeat(concertSchedule.id(), seatId);
    }
}
