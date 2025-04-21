package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.model.enums.PaymentType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wallet",
        indexes = {
                @Index(name = "idx_user_id",  columnList="user_id", unique = true),
                @Index(name = "idx_processed_at",  columnList="processed_at")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class WalletEntity {
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

    public static WalletEntity from(Wallet payment) {
        return WalletEntity.builder()
                .id(payment.getId())
                .user(UserEntity.builder().id(payment.getUserId()).build())
                .balance(payment.getBalance())
                .paymentType(payment.getPaymentType())
                .processedAt(payment.getProcessedAt())
                .build();
    }

    public Wallet of() {
        return Wallet.builder()
                .id(this.id)
                .userId(this.user.getId())
                .balance(this.balance)
                .paymentType(this.paymentType)
                .processedAt(this.processedAt)
                .build();
    }
}
