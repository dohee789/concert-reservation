package kr.hhplus.concert.domain.model.wallet;

import kr.hhplus.concert.domain.model.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class Wallet {
    private final Reservation reservation;
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

    public void pay(Reservation reservation, Float amount) {
        this.balance -= amount;
        this.processedAt = LocalDateTime.now();
        this.walletType = walletType.PAYMENT;
        reservation.getSeat().reserved();
    }
}
