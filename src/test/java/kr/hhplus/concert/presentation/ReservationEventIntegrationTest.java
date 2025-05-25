package kr.hhplus.concert.presentation;

import kr.hhplus.concert.TestcontainersConfiguration;
import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.facade.ReservationFacade;
import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import kr.hhplus.concert.infrastructure.client.MockReservationReporter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Sql("/init.sql")
@RecordApplicationEvents
class ReservationEventIntegrationTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ApplicationEvents events;

    @SpyBean
    private MockReservationReporter mockReservationReporter;

    @Test
    void Event_Published_When_Reservation_Succeed() {
        // given
        ReservationCommand command = new ReservationCommand(1L, 2L,3L);

        // when
        reservationFacade.makeReservation(command);

        // then
        assertThat(events.stream(ReservationCompletedEvent.class).count()).isEqualTo(1);

        // mockReporter가 실제로 호출되었는지 검증
        verify(mockReservationReporter, timeout(1000).times(1))
                .sendReservationToDataPlatform(any());

        // 또는 ArgumentCaptor로 실제 값 검증
        ArgumentCaptor<ReservationCompletedEvent> captor =
                ArgumentCaptor.forClass(ReservationCompletedEvent.class);

        verify(mockReservationReporter).sendReservationToDataPlatform(captor.capture());

        ReservationCompletedEvent event = captor.getValue();
        assertThat(event.concertScheduleId()).isEqualTo(2L);
        assertThat(event.reservedAt()).isNotNull();
    }
}

