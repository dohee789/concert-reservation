package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.repository.PaymentRepository;
import kr.hhplus.concert.infrastructure.entity.PaymentEntity;
import kr.hhplus.concert.infrastructure.entity.QueueEntity;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public Optional<Payment> findByUserId(Long userId) {
        return paymentJpaRepository.findById(userId)
                .map(PaymentEntity::of);
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        PaymentEntity paymentEntity = paymentJpaRepository.save(PaymentEntity.from(payment));
        return paymentEntity.of();
    }

}
