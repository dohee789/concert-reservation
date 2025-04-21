package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByUserId(Long userId);

    Payment save(Payment payment);
}
