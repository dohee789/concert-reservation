package kr.hhplus.concert.presentation.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationRequestDTO {
    private Long userId;
    private Long concertId;
    private Integer concertScheduleId;
    private Integer seatId;
}
