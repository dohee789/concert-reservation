package kr.hhplus.concert.domain.exception;

public class ConcurrencyReservationException extends RuntimeException{
    public ConcurrencyReservationException() {
        super("다른 사용자에 의해 좌석이 선점되었습니다. 다시 시도해주세요.");
    }
}
