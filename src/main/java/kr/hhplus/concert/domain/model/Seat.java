package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
@RequiredArgsConstructor
@Builder
public class Seat {
    private final ConcertSchedule concertSchedule;

    private Long id;
    private Long seatNumber;
    private Float price;

    private SeatStatus seatStatus;

    private Long version;

    public void reserved(){
        this.seatStatus = seatStatus.RESERVED;
    }

    public void notAssigned(){
        this.seatStatus = seatStatus.AVAILABLE;
    }

    public void pending(){
        this.seatStatus = seatStatus.PENDING;
    }
}
