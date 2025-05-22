package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.wallet.WalletHistory;

import java.util.Optional;


public interface WalletHistoryRepository {
    Optional<WalletHistory> findHistoryById(Long userId);

    WalletHistory save(WalletHistory walletHistory);
}
