package kr.hhplus.concert.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Reservation {
    private Integer id;
    private Long userId;
    private Integer concertScheduleId;
    private Integer seatId;
    private LocalDateTime reservedAt;

    public static Reservation create(Long userId, Integer concertScheduleId, Integer seatId) {
        return Reservation.builder()
                .userId(userId)
                .concertScheduleId(concertScheduleId)
                .seatId(seatId)
                .build();
    }

}
