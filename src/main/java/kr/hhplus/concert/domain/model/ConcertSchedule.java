package kr.hhplus.concert.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ConcertSchedule(
    Long id,
    LocalDateTime scheduleDateTime,
    Concert concert
) {}
