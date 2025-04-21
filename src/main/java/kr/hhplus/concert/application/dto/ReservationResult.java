package kr.hhplus.concert.application.dto;

import kr.hhplus.concert.domain.model.Reservation;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ReservationResult (
    Long userId,
    String concertName,
    LocalDateTime concertScheduleDate,
    Long seatNumber,
    LocalDateTime reservedAt
) {
    public static ReservationResult from(Reservation reservation) {
        return ReservationResult.builder()
                .userId(reservation.getQueue().getUserId())
                .concertName(reservation.getSeat().getConcertSchedule().concert().name())
                .concertScheduleDate(reservation.getSeat().getConcertSchedule().scheduleDateTime())
                .seatNumber(reservation.getSeat().getSeatNumber())
                .reservedAt(reservation.getReservedAt())
                .build();
    }
}
