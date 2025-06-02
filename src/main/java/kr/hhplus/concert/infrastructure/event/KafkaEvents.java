package kr.hhplus.concert.infrastructure.event;

import kr.hhplus.concert.domain.model.common.Events;
import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEvents implements Events {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String RESERVATION_TOPIC = "concert.reservation.completed";

    @Override
    public <T> void publish(T event) {
        // 즉시 발행하지 않고, 트랜잭션 커밋 후 발행하도록 내부 이벤트로 전환
        // 실제 Kafka 발행은 @TransactionalEventListener에서 처리
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationCompletedEvent(ReservationCompletedEvent event) {
        publishToKafka(RESERVATION_TOPIC, event);
    }

    private void publishToKafka(String topic, Object event) {
        try {
            String key = generateMessageKey(event);

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message to Kafka topic: {}, event: {}",
                            topic, event, ex);
                } else {
                    log.info("Successfully sent message to Kafka topic: {}, key: {}, offset: {}",
                            topic, key, result.getRecordMetadata().offset());
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event to Kafka: {}", event, e);
        }
    }

    private String generateMessageKey(Object event) {
        if (event instanceof ReservationCompletedEvent reservationEvent) {
            return "reservation:" + reservationEvent.concertScheduleId();
        }
        return "default";
    }
}