package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findByUserIdWithLock(Long userId);

    Wallet save(Wallet payment);
}
