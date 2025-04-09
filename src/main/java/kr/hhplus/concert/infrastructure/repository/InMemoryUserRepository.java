package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class InMemoryUserRepository implements UserRepository {
    private final Set<Long> existUsers = new HashSet<>();

    public InMemoryUserRepository() {
        // 유저가 이미 존재한다는 설정
        existUsers.add(1L);
        existUsers.add(2L);
        existUsers.add(3L);
    }

    public void existsUser(Long userId) {
        boolean existsUsers = existUsers.contains(userId);
        if (!existsUsers) {
            throw new UserNotFoundException(userId);
        }
    }

}
