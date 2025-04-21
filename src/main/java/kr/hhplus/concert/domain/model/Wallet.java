package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.PaymentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Wallet {
    private Long id;
    private Long userId;
    private Float amount;
    private Float balance;
    private LocalDateTime processedAt;

    private PaymentType paymentType;

    public static Wallet create(Long userId) {
        return Wallet.builder()
                .userId(userId)
                .balance(0F)
                .processedAt(LocalDateTime.now())
                .build();

    }
    public void charge(Float amount) {
        this.balance += amount;
        this.processedAt = LocalDateTime.now();
        this.paymentType = paymentType.CHARGE;
    }

    public void pay(Float amount) {
        this.balance -= amount;
        this.processedAt = LocalDateTime.now();
        this.paymentType = paymentType.PAYMENT;
    }
}
