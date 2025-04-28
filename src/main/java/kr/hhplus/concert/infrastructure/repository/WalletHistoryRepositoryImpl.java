package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.WalletHistory;
import kr.hhplus.concert.domain.repository.WalletHistoryRepository;
import kr.hhplus.concert.infrastructure.entity.WalletHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class WalletHistoryRepositoryImpl implements WalletHistoryRepository {

    private final WalletHistoryJpaRepository walletHistoryJpaRepository;

    @Override
    public Optional<WalletHistory> findHistoryById(Long userId) {
        return walletHistoryJpaRepository.findById(userId)
                .map(WalletHistoryEntity::of);
    }

    @Override
    @Transactional
    public WalletHistory save(WalletHistory walletHistory) {
        WalletHistoryEntity walletHistoryEntity = walletHistoryJpaRepository.save(WalletHistoryEntity.from(walletHistory));
        return walletHistoryEntity.of();
    }
}
