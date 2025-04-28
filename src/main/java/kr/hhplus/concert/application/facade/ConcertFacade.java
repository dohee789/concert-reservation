package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.service.ConcertScheduleService;
import kr.hhplus.concert.domain.service.ConcertService;
import kr.hhplus.concert.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConcertFacade {
    private QueueService queueService;
    private ConcertService concertService;
    private ConcertScheduleService concertScheduleService;

    public ConcertSchedule findSchedules(Long userId, Long id, LocalDateTime scheduleDate) {
        // 토큰 검증
        queueService.getValidatedQueue(userId);
        // 콘서트 찾기
        Concert concert = concertService.findConcertById(id);
        return concertScheduleService.findConcertSchedulesByIdAndDate(concert.id(), scheduleDate);
    }
}
