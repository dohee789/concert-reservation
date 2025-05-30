package kr.hhplus.concert.domain.model.queue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@RequiredArgsConstructor
public class Queue {
    Long id;
    Long userId;
    String token;
    LocalDateTime expiredAt;
    LocalDateTime enteredAt;

    private QueueStatus queueStatus;

    private Long version;

    private static final int MAX_QUEUE_SIZE = 100;

    public static Queue generateToken(Long userId) {
        return Queue.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .enteredAt(LocalDateTime.now())
                .queueStatus(QueueStatus.ACTIVE)
                .build();
    }

    @JsonIgnore
    public boolean isActive() {
        return this.queueStatus == QueueStatus.ACTIVE;
    }

    public void pending() {
        this.queueStatus = QueueStatus.WAITING;
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public void expireToken() {
        if (isExpired()) {
            this.queueStatus = QueueStatus.EXPIRED;
        }
    }
}
