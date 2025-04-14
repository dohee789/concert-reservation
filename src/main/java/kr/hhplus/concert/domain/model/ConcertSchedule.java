package kr.hhplus.concert.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ConcertSchedule (
    Integer id,
    Long concertId,
    LocalDate scheduleDate,
    LocalTime startTime
) {}
