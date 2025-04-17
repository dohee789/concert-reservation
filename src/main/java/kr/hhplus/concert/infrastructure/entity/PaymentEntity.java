package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.model.enums.PaymentType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    private Float balance;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDateTime processedAt;

    public static PaymentEntity from(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .user(UserEntity.builder().id(payment.getUserId()).build())
                .balance(payment.getBalance())
                .paymentType(payment.getPaymentType())
                .processedAt(payment.getProcessedAt())
                .build();
    }

    public Payment of() {
        return Payment.builder()
                .id(this.id)
                .userId(this.user.getId())
                .balance(this.balance)
                .paymentType(this.paymentType)
                .processedAt(this.processedAt)
                .build();
    }
}
