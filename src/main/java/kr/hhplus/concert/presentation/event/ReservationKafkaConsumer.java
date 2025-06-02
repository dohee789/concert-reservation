package kr.hhplus.concert.presentation.event;

import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import kr.hhplus.concert.infrastructure.client.MockReservationReporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationKafkaConsumer {

    private final MockReservationReporter mockReservationReporter;

    @KafkaListener(
            topics = "concert.reservation.completed",
            groupId = "concert-reservation-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleReservationCompleted(
            @Payload ReservationCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Received reservation completed event from Kafka - Topic: {}, Partition: {}, Offset: {}, Event: {}",
                    topic, partition, offset, event);

            // 외부 데이터 플랫폼으로 전송
            mockReservationReporter.sendReservationToDataPlatform(event);

            // 수동으로 offset commit
            acknowledgment.acknowledge();

            log.info("Successfully processed reservation event: {}", event);

        } catch (Exception e) {
            log.error("Error processing reservation event: {}", event, e);
            // 에러 발생 시 acknowledge 하지 않아서 재처리 가능하도록 함
            // 필요에 따라 DLQ(Dead Letter Queue) 처리 로직 추가 가능
        }
    }
}