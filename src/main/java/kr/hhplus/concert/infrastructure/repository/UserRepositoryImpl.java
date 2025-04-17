package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private UserJpaRepository userJpaRepository;

    public void existsUser(Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

}
