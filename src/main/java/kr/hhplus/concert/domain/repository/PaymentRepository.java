package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Float getBalance(Long userId);

    Optional<Payment> findByUserId(Long userId);

    void save(Payment payment);
}
