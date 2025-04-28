package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.PaymentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class WalletHistory {
    private final Wallet wallet;

    private Long id;
    private Long userId;
    private Float balance;
    private LocalDateTime processedAt;

    private PaymentType paymentType;

    public static WalletHistory create(Wallet wallet) {
        return WalletHistory.builder()
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .processedAt(wallet.getProcessedAt())
                .paymentType(wallet.getPaymentType())
                .build();
    }
}
