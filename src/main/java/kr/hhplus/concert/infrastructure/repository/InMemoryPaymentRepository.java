package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.repository.PaymentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPaymentRepository implements PaymentRepository {
    private Map<Long, Payment> store = new HashMap<>();

    public Float getBalance(Long userId) {
        return store.get(userId).getBalance();
    }

    public void save(Payment payment) {
        store.put(payment.getUserId(), payment);
    }

    public Optional<Payment> findByUserId(Long userId) {
        return Optional.ofNullable(store.get(userId));
    }

}
