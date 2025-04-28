package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import kr.hhplus.concert.infrastructure.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository walletJpaRepository;

    @Override
    public Optional<Wallet> findByUserIdWithLock(Long userId) {
        return walletJpaRepository.findByUserIdWithLock(userId)
                .map(WalletEntity::of);
    }

    @Override
    public Optional<Wallet> findByUserId(Long userId) {
        return walletJpaRepository.findById(userId)
                .map(WalletEntity::of);
    }

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        WalletEntity walletEntity = walletJpaRepository.save(WalletEntity.from(wallet));
        return walletEntity.of();
    }

}
