package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConcertScheduleService {
    private ConcertScheduleRepository concertScheduleRepository;

    public ConcertSchedule findConcertSchedulesByIdAndDate(Long concertId, LocalDateTime scheduleDate) {
        List<ConcertSchedule> schedules = concertScheduleRepository.findConcertSchedulesById(concertId);

        return schedules.stream()
                .filter(schedule -> schedule.scheduleDateTime().equals(scheduleDate))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 날짜의 콘서트 스케줄이 없습니다."));
    }

}
