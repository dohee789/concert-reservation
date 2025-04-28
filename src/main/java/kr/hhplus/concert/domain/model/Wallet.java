package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.WalletType;
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

    private WalletType walletType;

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
        this.walletType = walletType.CHARGE;
    }

    public void pay(Float amount) {
        this.balance -= amount;
        this.processedAt = LocalDateTime.now();
        this.walletType = walletType.PAYMENT;
    }
}
