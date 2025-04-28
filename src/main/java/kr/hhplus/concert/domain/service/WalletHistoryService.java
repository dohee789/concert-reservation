package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.model.WalletHistory;
import kr.hhplus.concert.domain.repository.WalletHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletHistoryService {
    private final WalletHistoryRepository walletHistoryRepository;

    public Optional<WalletHistory> findHistoryById(Long userId) {
        Optional<WalletHistory> walletHistory = walletHistoryRepository.findHistoryById(userId);
        return walletHistory;
    }

    public void save(Wallet wallet) {
        WalletHistory walletHistory = WalletHistory.create(wallet);
        walletHistoryRepository.save(walletHistory);
    }

}
