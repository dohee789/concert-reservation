package kr.hhplus.concert.domain.service;


import kr.hhplus.concert.domain.exception.InSufficientBalanceException;
import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.repository.PaymentRepository;
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
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @DisplayName("사용자의 잔액을 조회할 수 있다")
    @Test
    void getBalance_success() {
        // given
        Long userId = 1L;
        Float expectedBalance = 1000F;
        Mockito.when(paymentRepository.getBalance(userId)).thenReturn(expectedBalance);
        // when
        Float balance = paymentService.getBalance(userId);
        // then
        assertThat(balance).isEqualTo(expectedBalance);
    }

    @DisplayName("사용자의 잔액을 충전할 수 있다")
    @Test
    void chargeBalance_success() {
        // given
        Long userId = 1L;
        Float amount = 500F;
        Payment existing = Payment.create(userId);
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        // when
        Payment updated = paymentService.chargeBalance(userId, amount);
        // then
        assertThat(updated.getBalance()).isEqualTo(1500F);
        Mockito.verify(paymentRepository).save(updated);
    }

    @DisplayName("사용자가 결제 시 잔액이 부족하면 예외가 발생한다")
    @Test
    void payBalance_insufficientBalance() {
        // given
        Long userId = 1L;
        Float amount = 500F;
        Payment existing = Payment.create(userId);
        Mockito.when(paymentRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        // when
        // then
        assertThatThrownBy(() -> paymentService.payBalance(userId, amount))
                .isInstanceOf(InSufficientBalanceException.class)
                .hasMessageContaining("잔액이 부족합니다");

        Mockito.verify(paymentRepository, Mockito.never()).save(Mockito.any());
    }



}