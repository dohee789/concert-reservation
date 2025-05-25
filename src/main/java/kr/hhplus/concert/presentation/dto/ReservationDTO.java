package kr.hhplus.concert.presentation.dto;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import lombok.Builder;

import java.time.LocalDateTime;


public class ReservationDTO {

    @Builder
    public record ReservationRequest (
        Long userId,
        Long concertId,
        Long concertScheduleId,
        Long seatId
    ) {
        public ReservationCommand toCommand(String token) {
            return ReservationCommand.builder()
                    .userId(this.userId)
                    .concertScheduleId(this.concertScheduleId)
                    .seatId(this.seatId)
                    .build();
        }
    }

    @Builder
    public record ReservationResponse (
        Long userId,
        String concertName,
        LocalDateTime concertScheduleDate,
        Long seatNumber
    ) {
        public static ReservationResponse of(ReservationResult result) {
            return ReservationResponse.builder()
                    .userId(result.userId())
                    .concertName(result.concertName())
                    .concertScheduleDate(result.concertScheduleDate())
                    .seatNumber(result.seatNumber())
                    .build();
        }
    }
}
