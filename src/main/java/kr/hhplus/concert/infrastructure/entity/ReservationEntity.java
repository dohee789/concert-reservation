package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Reservation;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation",
        indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_processed_at",  columnList="processed_at")},
        uniqueConstraints = @UniqueConstraint(name = "unique_concert_seat", columnNames = {"seat_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private SeatEntity seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime reservedAt;

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .id(reservation.getId())
                .user(UserEntity.builder().id(reservation.getQueue().getUserId()).build())
                .seat(SeatEntity.from(reservation.getSeat()))
                .reservedAt(reservation.getReservedAt())
                .build();
    }

    public Reservation of() {
        return Reservation.builder()
                .id(this.id)
                .userId(this.user.getId())
                .seat(this.seat.of())
                .reservedAt(this.seat.getConcertSchedule().getScheduleDateTime())
                .build();
    }
}
