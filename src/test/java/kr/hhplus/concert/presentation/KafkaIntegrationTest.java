package kr.hhplus.concert.presentation;

import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import kr.hhplus.concert.infrastructure.client.MockReservationReporter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        },
        topics = {"concert.reservation.completed"}
)
@DirtiesContext
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    private MockReservationReporter mockReservationReporter;

    @Test
    void reservationEventPublish_by_kafka_and_consume() throws InterruptedException {
        // Given
        ReservationCompletedEvent event = new ReservationCompletedEvent(
                123L,
                LocalDateTime.now()
        );

        // Consumer 설정
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "test-group", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, ReservationCompletedEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps);

        ContainerProperties containerProperties = new ContainerProperties("concert.reservation.completed");
        KafkaMessageListenerContainer<String, ReservationCompletedEvent> container =
                new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        BlockingQueue<ConsumerRecord<String, ReservationCompletedEvent>> records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, ReservationCompletedEvent>) records::add);

        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());

        // When
        kafkaTemplate.send("concert.reservation.completed", "reservation:123", event);

        // Then
        ConsumerRecord<String, ReservationCompletedEvent> received =
                records.poll(10, TimeUnit.SECONDS);

        assertThat(received).isNotNull();
        assertThat(received.key()).isEqualTo("reservation:123");
        assertThat(received.value().concertScheduleId()).isEqualTo(123L);
        assertThat(received.value().reservedAt()).isNotNull();

        container.stop();
    }

    @Test
    void reservationCompleteConsumer_call_MockReservationReporter() throws InterruptedException {
        // Given
        ReservationCompletedEvent event = new ReservationCompletedEvent(
                456L,
                LocalDateTime.now()
        );

        // When
        kafkaTemplate.send("concert.reservation.completed", "reservation:456", event);

        // Then - 실제 Consumer가 처리할 때까지 대기
        Thread.sleep(2000);

        ArgumentCaptor<ReservationCompletedEvent> eventCaptor =
                ArgumentCaptor.forClass(ReservationCompletedEvent.class);

        verify(mockReservationReporter, timeout(5000))
                .sendReservationToDataPlatform(eventCaptor.capture());

        ReservationCompletedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.concertScheduleId()).isEqualTo(456L);
    }
}