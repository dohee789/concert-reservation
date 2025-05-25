package kr.hhplus.concert.domain.model.reservation;

import java.time.LocalDateTime;

public record ReservationCompletedEvent(
        Long concertScheduleId,
        LocalDateTime reservedAt
) {}
