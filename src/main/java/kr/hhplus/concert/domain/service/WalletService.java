package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public Wallet getBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseGet(() -> Wallet.create(userId));
        return wallet;
    }

    public Wallet chargeBalance(Long userId, Float amount) {
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseGet(() -> Wallet.create(userId));

        wallet.charge(amount);
        return walletRepository.save(wallet);
    }

    public Wallet payMoney(Long userId, Float amount) {
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (wallet.getBalance() < amount) {
            throw new InSufficientBalanceException(wallet.getBalance());
        }

        wallet.pay(amount);
        return walletRepository.save(wallet);
    }

}
