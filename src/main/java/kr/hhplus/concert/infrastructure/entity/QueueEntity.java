package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue")
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

    public static QueueEntity from(Queue queue) {
        return QueueEntity.builder()
                .id(queue.getId())
                .user(UserEntity.builder().id(queue.getUserId()).build())
                .token(queue.getToken())
                .queueStatus(queue.getQueueStatus())
                .expiredAt(queue.getExpiredAt())
                .enteredAt(queue.getEnteredAt())
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
                .build();
    }
}
