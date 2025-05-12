package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.model.WalletHistory;
import lombok.*;

@Entity
@Table(name = "wallet_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class WalletHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WalletEntity wallet;

    public static WalletHistoryEntity from(WalletHistory walletHistory) {
        return WalletHistoryEntity.builder()
                .id(walletHistory.getId())
                .wallet(WalletEntity.from(walletHistory.getWallet()))
                .build();
    }

    public WalletHistory of() {
        return WalletHistory.builder()
                .id(this.id)
                .userId(this.wallet.getUser().getId())
                .balance(this.wallet.getBalance())
                .processedAt(this.wallet.getProcessedAt())
                .paymentType(this.wallet.getWalletType())
                .build();
    }
}
