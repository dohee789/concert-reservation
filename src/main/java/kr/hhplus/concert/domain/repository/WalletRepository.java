package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.wallet.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findByUserIdWithLock(Long userId);

    Optional<Wallet> findByUserId(Long userId);

    Wallet save(Wallet payment);
}
