package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {
}
