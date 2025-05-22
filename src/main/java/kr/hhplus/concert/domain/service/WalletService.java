package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.model.reservation.Reservation;
import kr.hhplus.concert.domain.model.wallet.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public Wallet getBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseGet(() -> Wallet.create(userId));
        return wallet;
    }

    @Transactional
    public Wallet chargeBalance(Long userId, Float amount) {
        Wallet wallet = walletRepository.findByUserIdWithLock(userId)
                .orElseGet(() -> Wallet.create(userId));

        wallet.charge(amount);
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet payMoney(Reservation reservation, Float amount) {
        Wallet wallet = walletRepository.findByUserIdWithLock(reservation.getQueue().getUserId())
                .orElseThrow(() -> new UserNotFoundException(reservation.getQueue().getUserId()));

        if (wallet.getBalance() < amount) {
            throw new InSufficientBalanceException(wallet.getBalance());
        }

        wallet.pay(reservation, amount);
        return walletRepository.save(wallet);
    }

}
