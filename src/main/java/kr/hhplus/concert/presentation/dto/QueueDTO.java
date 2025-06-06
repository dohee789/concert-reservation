package kr.hhplus.concert.presentation.dto;

import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.model.queue.QueueStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public class QueueDTO {
    @Builder
    public record QueueRequest (
            Long userId,
            Long concertScheduleId
    ) {
    }

    @Builder
    public record QueueResponse (
            String token,
            QueueStatus status,
            LocalDateTime expiredAt,
            LocalDateTime enteredAt
    ) {
        public static QueueResponse of(Queue token) {
            return QueueResponse.builder()
                    .token(token.getToken())
                    .status(token.getQueueStatus())
                    .enteredAt(token.getEnteredAt())
                    .expiredAt(token.getExpiredAt())
                    .build();
        }
    }
}
