package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.WalletHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletHistoryJpaRepository extends JpaRepository<WalletHistoryEntity, Long> {
}
