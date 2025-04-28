package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private UserJpaRepository userJpaRepository;

    @Override
    public void existsUser(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

}
