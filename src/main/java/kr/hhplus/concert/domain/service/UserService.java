package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void CheckExistsUser(Long userId) {
        userRepository.existsUser(userId);
    }
}
