package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WalletService {
    private final WalletRepository paymentRepository;

    public Wallet getBalance(Long userId) {
        Wallet payment = paymentRepository.findByUserId(userId)
                .orElseGet(() -> Wallet.create(userId));
        return payment;
    }

    public Wallet chargeBalance(Long userId, Float amount) {
        Wallet payment = paymentRepository.findByUserId(userId)
                .orElseGet(() -> Wallet.create(userId));

        payment.charge(amount);
        return paymentRepository.save(payment);
    }

    public Wallet payBalance(Long userId, Float amount) {
        Wallet payment = paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (payment.getBalance() < amount) {
            throw new InSufficientBalanceException(payment.getBalance());
        }

        payment.pay(amount);
        return paymentRepository.save(payment);
    }

}
