package kr.hhplus.concert.presentation.dto;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.domain.model.Reservation;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class ReservationDTO {

    @Builder
    public record ReservationRequest (
        Long userId,
        Long concertId,
        Integer concertScheduleId,
        Integer seatId
    ) {
        public ReservationCommand toCommand(String token) {
            return ReservationCommand.builder()
                    .userId(this.userId)
                    .concertId(this.concertId)
                    .concertScheduleId(this.concertScheduleId)
                    .seatId(this.seatId)
                    .build();
        }
    }

    @Builder
    public record ReservationResponse (
        Long userId,
        String concertName,
        LocalDate concertScheduleDate,
        Integer seatNumber,
        LocalDateTime reservedAt
    ) {
        public static ReservationResponse of(ReservationResult result) {
            return ReservationResponse.builder()
                    .userId(result.userId())
                    .concertName(result.concertName())
                    .concertScheduleDate(result.concertScheduleDate())
                    .seatNumber(result.seatNumber())
                    .reservedAt(result.reservedAt())
                    .build();
        }
    }
}
