package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
import lombok.*;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long seatNumber;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ConcertScheduleEntity concertSchedule;

    public static SeatEntity from(Seat seat) {
        return SeatEntity.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .price(seat.getPrice())
                .seatStatus(seat.getSeatStatus())
                .concertSchedule(ConcertScheduleEntity.from(seat.getConcertSchedule()))
                .build();
    }

    public Seat of() {
        return Seat.builder()
                .id(this.id)
                .concertSchedule(this.concertSchedule.of())
                .seatNumber(this.seatNumber)
                .price(this.price)
                .seatStatus(this.seatStatus)
                .build();
    }
}
