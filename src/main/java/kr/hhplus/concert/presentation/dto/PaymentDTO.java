package kr.hhplus.concert.presentation.dto;

import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.model.enums.PaymentType;
import lombok.Builder;

import java.time.LocalDateTime;

public class PaymentDTO {

    @Builder
    public record PaymentRequest (
        Long userId,
        Float balance,
        Float amount
    ) {
    }

    @Builder
    public record PaymentResponse (
        Long id,
        Float balance,
        LocalDateTime processedAt
    ) {
        public static PaymentResponse of(Payment payment) {
            return PaymentResponse.builder()
                    .id(payment.getId())
                    .balance(payment.getBalance())
                    .processedAt(payment.getProcessedAt())
                    .build();
        }
    }
}

