package kr.hhplus.concert.domain.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Builder
public record ConcertSchedule(
    Long id,
    LocalDateTime scheduleDateTime,
    Concert concert
) {}
