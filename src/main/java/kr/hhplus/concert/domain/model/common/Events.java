package kr.hhplus.concert.domain.model.common;

public interface Events {
    <T> void publish(T event);
}
