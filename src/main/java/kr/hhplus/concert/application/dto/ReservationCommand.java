package kr.hhplus.concert.application.dto;

import lombok.Builder;

@Builder
public record ReservationCommand (
    Long userId,
    Long concertId,
    Long concertScheduleId,
    Long seatId
){
}
