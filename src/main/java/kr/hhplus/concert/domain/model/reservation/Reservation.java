package kr.hhplus.concert.domain.model.reservation;

import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.model.seat.Seat;
import kr.hhplus.concert.domain.model.seat.SeatStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Builder
public class Reservation {
    private final Queue queue;
    private final Seat seat;
    private Long id;
    private Long userId;
    private LocalDateTime reservedAt;

    public static Reservation create(Queue queue, Seat seat) {
        if (!queue.isActive() && queue == null) {
            throw new IllegalStateException("예약 가능한 상태가 아닙니다.");
        }
        if (seat.getSeatStatus() != SeatStatus.AVAILABLE && seat == null) {
            throw new IllegalStateException("해당 좌석은 예약할 수 없습니다.");
        }

        seat.pending();
        queue.pending();

        return Reservation.builder()
                .queue(queue)
                .seat(seat)
                .userId(queue.getUserId())
                .reservedAt(LocalDateTime.now())
                .build();
    }
}

