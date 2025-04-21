package kr.hhplus.concert.infrastructure.entity;


import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_schedule")
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

    public static ConcertScheduleEntity from(ConcertSchedule concertSchedule) {
        return ConcertScheduleEntity.builder()
                .id(concertSchedule.id())
                .concert(ConcertEntity.from(concertSchedule.concert()))
                .scheduleDateTime(concertSchedule.scheduleDateTime())
                .build();
    }

    public ConcertSchedule of() {
        return ConcertSchedule.builder()
                .id(this.id)
                .concert(this.concert.of())
                .scheduleDateTime(this.scheduleDateTime)
                .build();
    }
}
