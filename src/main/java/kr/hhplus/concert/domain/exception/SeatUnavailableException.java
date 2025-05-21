package kr.hhplus.concert.domain.exception;

import kr.hhplus.concert.domain.model.seat.SeatStatus;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(SeatStatus seatStatus){
        super("예약이 불가능한 좌석입니다. 좌석상태 : " + seatStatus);
    }
}
