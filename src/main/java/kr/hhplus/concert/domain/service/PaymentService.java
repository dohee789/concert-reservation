package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.repository.PaymentRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Float getBalance(Long userId) {
        return paymentRepository.getBalance(userId);
    }

    public Payment chargeBalance(Long userId, Float amount) {
        Payment payment = paymentRepository.findByUserId(userId)
                .orElseGet(() -> Payment.create(userId, 0F));

        payment.charge(amount);
        paymentRepository.save(payment);
        return payment;
    }

    public Payment payBalance(Long userId, Float amount) {
        Payment payment = paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (payment.getBalance() < amount) {
            throw new InSufficientBalanceException(payment.getBalance());
        }

        payment.pay(amount);
        paymentRepository.save(payment);
        return payment;
    }

}
