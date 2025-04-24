package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(kr.hhplus.concert.TestcontainersConfiguration.class)
@Sql("/init.sql")
class ReservationFacadeTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Test
    void preventSeatDuplication_WithOptimisticLock_OnConcurrentReservation() throws InterruptedException {
        int threadCount = 5;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        for(int i=0; i< threadCount;i++) {
            int userId = i;
            service.submit(() -> {
                ReservationCommand command = ReservationCommand.builder()
                        .userId((long)userId)
                        .concertId(10L)
                        .concertScheduleId(1L)
                        .seatId(1L)
                        .build();
                try {
                    ReservationResult result = reservationFacade.makeReservation(command);
                    System.out.println("Result for user " + userId + ": " + result);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Exception for user " + userId + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        service.shutdown();
        assertEquals(1, successCount.get());
    }

}