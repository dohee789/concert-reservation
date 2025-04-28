package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.WalletType;
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

    private WalletType paymentType;

    public static WalletHistory create(Wallet wallet) {
        return WalletHistory.builder()
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .processedAt(wallet.getProcessedAt())
                .paymentType(wallet.getWalletType())
                .build();
    }
}
