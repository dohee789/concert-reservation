package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.concert.ConcertSchedule;

import java.util.List;

public interface ConcertScheduleRepository {
    List<ConcertSchedule> findConcertSchedulesById(Long concertId);
}
