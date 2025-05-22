package kr.hhplus.concert.domain.model.concert;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;

import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Builder
public record ConcertSchedule(
    Long id,
    LocalDateTime scheduleDateTime,
    LocalDateTime openedAt,
    Concert concert
) {}
