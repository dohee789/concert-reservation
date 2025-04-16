package kr.hhplus.concert.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Payment {
    private Long id;
    private Long userId;
    private Float balance;
    private LocalDateTime processedAt;

    public static Payment create(Long userId, Float balance) {
        return Payment.builder()
                .userId(userId)
                .balance(balance)
                .processedAt(LocalDateTime.now())
                .build();

    }
    public void charge(Float balance) {
        this.balance += balance;
        this.processedAt = LocalDateTime.now();
    }

    public void pay(Float balance) {
        this.balance -= balance;
        this.processedAt = LocalDateTime.now();
    }
}
