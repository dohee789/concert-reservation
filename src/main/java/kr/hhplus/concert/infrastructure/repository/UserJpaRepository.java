package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
