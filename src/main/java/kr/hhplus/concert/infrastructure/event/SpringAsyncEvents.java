package kr.hhplus.concert.infrastructure.event;

import kr.hhplus.concert.domain.model.common.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringAsyncEvents implements Events {
    private final ApplicationEventPublisher publisher;

    @Override
    @Async
    public <T> void publish(T event) {
        publisher.publishEvent(event);
    }
}
