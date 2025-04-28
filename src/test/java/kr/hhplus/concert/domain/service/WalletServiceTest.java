package kr.hhplus.concert.domain.service;


import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @DisplayName("사용자의 잔액을 충전할 수 있다")
    @Test
    void chargeBalance_success() {
        // given
        Long userId = 1L;
        Float amount = 500F;
        Wallet existing = Wallet.create(userId);
        Mockito.when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        // when
        Wallet updated = walletService.chargeBalance(userId, amount);
        // then
        assertThat(updated.getBalance()).isEqualTo(1500F);
        Mockito.verify(walletRepository).save(updated);
    }

    @DisplayName("사용자가 결제 시 잔액이 부족하면 예외가 발생한다")
    @Test
    void payBalance_insufficientBalance() {
        // given
        Long userId = 1L;
        Float amount = 500F;
        Wallet existing = Wallet.create(userId);
        Mockito.when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        // when
        // then
        assertThatThrownBy(() -> walletService.payMoney(userId, amount))
                .isInstanceOf(InSufficientBalanceException.class)
                .hasMessageContaining("잔액이 부족합니다");

        Mockito.verify(walletRepository, Mockito.never()).save(Mockito.any());
    }
}