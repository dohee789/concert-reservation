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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    void paymentConcurrencyControl_With_PessimisticLock() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    walletFacade.wallet(1L);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Wallet wallet = walletRepository.findByUserId(1L).orElseThrow();
        System.out.println("In user wallet = " + wallet.getBalance());
    }
}