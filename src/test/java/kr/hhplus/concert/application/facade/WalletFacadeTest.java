package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import kr.hhplus.concert.infrastructure.entity.WalletEntity;
import kr.hhplus.concert.infrastructure.repository.WalletJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
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
class WalletFacadeTest {

    @Autowired
    private WalletFacade walletFacade;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void paymentConcurrencyControl_With_PessimisticLockAndDistributeLock() throws InterruptedException {
        int threadCount = 4000;
        Long reservationId = 1L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 1; i < threadCount+1; i++) {
            executor.execute(() -> {
                try {
                    walletFacade.wallet((long)1, reservationId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(1, successCount.get());
        Wallet wallet = walletRepository.findByUserId(1L).orElseThrow();
        assertEquals(0.0, wallet.getBalance(), 0.0);
    }
}