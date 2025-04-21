package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.WalletHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletHistoryJpaRepository extends JpaRepository<WalletHistoryEntity, Long> {
}
