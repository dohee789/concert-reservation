package kr.hhplus.concert.presentation.dto;

import kr.hhplus.concert.domain.model.wallet.Wallet;
import lombok.Builder;

import java.time.LocalDateTime;

public class BalanceDTO {

    @Builder
    public record BalanceRequest (
        Long userId,
        Long paymentId
    ) {
    }

    @Builder
    public record BalanceResponse (
        Long id,
        Float balance,
        LocalDateTime processedAt
    ) {
        public static BalanceResponse of(Wallet payment) {
            return BalanceResponse.builder()
                    .id(payment.getId())
                    .balance(payment.getBalance())
                    .processedAt(payment.getProcessedAt())
                    .build();
        }
    }
}

