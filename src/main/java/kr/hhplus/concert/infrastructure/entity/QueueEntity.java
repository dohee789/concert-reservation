package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.model.queue.QueueStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue",
        indexes = {
                @Index(name = "idx_queue_user_id",  columnList="user_id", unique = true),
                @Index(name = "idx_queue_token",  columnList="token", unique = true),
                @Index(name = "idx_queue_queue_status",  columnList="queue_status")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class QueueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatus queueStatus;

    private LocalDateTime expiredAt;
    private LocalDateTime enteredAt;

    @Version
    @ColumnDefault("0")
    private Long version;

    public static QueueEntity from(Queue queue) {
        return QueueEntity.builder()
                .id(queue.getId())
                .user(UserEntity.builder().id(queue.getUserId()).build())
                .token(queue.getToken())
                .queueStatus(queue.getQueueStatus())
                .expiredAt(queue.getExpiredAt())
                .enteredAt(queue.getEnteredAt())
                .version(queue.getVersion())
                .build();
    }

    public Queue of() {
        return Queue.builder()
                .id(this.id)
                .userId(this.user.getId())
                .token(this.token)
                .queueStatus(this.queueStatus)
                .expiredAt(this.expiredAt)
                .enteredAt(this.enteredAt)
                .version(this.getVersion())
                .build();
    }
}
