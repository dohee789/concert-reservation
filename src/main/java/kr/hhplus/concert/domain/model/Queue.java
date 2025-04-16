package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.QueueStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Queue {
    Long id;
    Long userId;
    String token;
    LocalDateTime expiredAt;
    LocalDateTime enteredAt;

    private QueueStatus queueStatus;

    public static Queue generateToken(Long userId) {
        return Queue.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .enteredAt(LocalDateTime.now())
                .queueStatus(QueueStatus.ACTIVE)
                .build();
    }

    public boolean isActive() {
        return this.queueStatus == QueueStatus.ACTIVE;
    }

    public void pending() {
        this.queueStatus = QueueStatus.WAITING;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public void expireToken() {
        if (isExpired()) {
            this.queueStatus = QueueStatus.EXPIRED;
        }
    }
}
