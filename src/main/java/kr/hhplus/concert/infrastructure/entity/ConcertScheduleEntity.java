package kr.hhplus.concert.infrastructure.entity;


import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_schedule",
        indexes = @Index(name = "idx_concert_id", columnList = "concert_id"),
        uniqueConstraints = @UniqueConstraint(name = "unique_concert_schedule", columnNames = {"concert_id", "schedule_date_time"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class ConcertScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ConcertEntity concert;

    @Column(nullable = false)
    private LocalDateTime scheduleDateTime;

    @Column(nullable = false)
    private LocalDateTime openedAt;

    public static ConcertScheduleEntity from(ConcertSchedule concertSchedule) {
        return ConcertScheduleEntity.builder()
                .id(concertSchedule.id())
                .concert(ConcertEntity.from(concertSchedule.concert()))
                .scheduleDateTime(concertSchedule.scheduleDateTime())
                .openedAt(concertSchedule.openedAt())
                .build();
    }

    public ConcertSchedule of() {
        return ConcertSchedule.builder()
                .id(this.id)
                .concert(this.concert.of())
                .scheduleDateTime(this.scheduleDateTime)
                .openedAt(this.openedAt)
                .build();
    }
}
