package kr.hhplus.concert.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("존재하지 않는 유저입니다. userId = " + userId);
    }
}
