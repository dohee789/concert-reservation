package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.SeatStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Seat {
    private Integer id;
    private Integer concertScheduleId;
    private Integer seatNumber;
    private Float price;

    private SeatStatus seatStatus;

    public Seat(Integer id, Integer concertScheduleId, Integer seatNumber, Float price, SeatStatus seatStatus) {
        this.id = id;
        this.concertScheduleId = concertScheduleId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.seatStatus = seatStatus;
    }

    public void isReserved(){
        this.seatStatus = seatStatus.RESERVED;
    }
}
