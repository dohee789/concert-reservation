package kr.hhplus.concert.domain.exception;

public class InSufficientBalanceException extends RuntimeException {
    public InSufficientBalanceException(Float balance){
        super("잔액이 부족합니다. 잔액 : " + balance);
    }
}
