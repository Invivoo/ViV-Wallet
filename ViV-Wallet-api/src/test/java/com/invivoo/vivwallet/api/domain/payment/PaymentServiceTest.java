package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    private static final User TEST_USER = new User(2L, "Test user");

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void should_return_empty_list_when_find_payments_for_unknown_user() {
        //Given
        long unknwonUserId = 1L;
        when(userRepository.findById(unknwonUserId)).thenReturn(Optional.empty());
        PaymentService paymentService = new PaymentService(userRepository, paymentRepository);

        //When
        List<PaymentDto> paymentDtos = paymentService.findAllByReceiver(unknwonUserId);

        //Then
        verify(userRepository).findById(unknwonUserId);
        assertThat(paymentDtos).isEmpty();
    }

    @Test
    public void should_return_empty_list_when_find_payments_for_user_without_payment() {
        //Given
        User testUser = TEST_USER;
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(paymentRepository.findAllByReceiverOrderByDateDesc(testUser)).thenReturn(Collections.emptyList());
        PaymentService paymentService = new PaymentService(userRepository, paymentRepository);

        //When
        List<PaymentDto> paymentDtos = paymentService.findAllByReceiver(testUser.getId());

        //Then
        verify(userRepository).findById(testUser.getId());
        verify(paymentRepository).findAllByReceiverOrderByDateDesc(testUser);
        assertThat(paymentDtos).isEmpty();
    }

    @Test
    public void should_return_one_payment_with_250_as_amount_when_find_payments_for_user_with_one_action_of_50_viv() {
        //Given
        User testUser = TEST_USER;
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        BigDecimal actionViv = BigDecimal.valueOf(50);
        Payment payment = Payment.builder()
                                 .id(1L)
                                 .date(LocalDateTime.of(2020, Month.JULY, 1, 12, 0))
                                 .receiver(testUser)
                                 .actions(List.of(Action.builder().id(1L).viv(actionViv).build())).build();
        when(paymentRepository.findAllByReceiverOrderByDateDesc(testUser)).thenReturn(List.of(payment));
        PaymentService paymentService = new PaymentService(userRepository, paymentRepository);

        //When
        List<PaymentDto> paymentDtos = paymentService.findAllByReceiver(testUser.getId());

        //Then
        verify(userRepository).findById(testUser.getId());
        verify(paymentRepository).findAllByReceiverOrderByDateDesc(testUser);
        PaymentDto expectedPaymentDto = PaymentDto.builder()
                                                  .id(payment.getId())
                                                  .userId(testUser.getId())
                                                  .date(payment.getDate())
                                                  .viv(actionViv)
                                                  .amount(BigDecimal.valueOf(250)).build();
        assertThat(paymentDtos).containsExactly(expectedPaymentDto);
    }

    @Test
    public void should_return_one_payment_with_500_as_amount_when_find_payments_for_user_with_2_actions_of_50_viv() {
        //Given
        User testUser = TEST_USER;
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Payment payment = Payment.builder()
                                 .id(1L)
                                 .date(LocalDateTime.of(2020, Month.JULY, 1, 12, 0))
                                 .receiver(testUser)
                                 .actions(List.of(
                                         Action.builder().id(1L).viv(BigDecimal.valueOf(50)).build(),
                                         Action.builder().id(2L).viv(BigDecimal.valueOf(50)).build())).build();
        when(paymentRepository.findAllByReceiverOrderByDateDesc(testUser)).thenReturn(List.of(payment));
        PaymentService paymentService = new PaymentService(userRepository, paymentRepository);

        //When
        List<PaymentDto> paymentDtos = paymentService.findAllByReceiver(testUser.getId());

        //Then
        verify(userRepository).findById(testUser.getId());
        verify(paymentRepository).findAllByReceiverOrderByDateDesc(testUser);
        PaymentDto expectedPaymentDto = PaymentDto.builder()
                                                  .id(payment.getId())
                                                  .userId(testUser.getId())
                                                  .date(payment.getDate())
                                                  .viv(BigDecimal.valueOf(100))
                                                  .amount(BigDecimal.valueOf(500)).build();
        assertThat(paymentDtos).containsExactly(expectedPaymentDto);
    }

    @Test
    public void should_return_2_payment_when_find_2_payments_for_user() {
        //Given
        User testUser = TEST_USER;
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Payment payment1 = Payment.builder()
                                  .id(1L)
                                  .date(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0))
                                  .receiver(testUser)
                                  .actions(List.of(Action.builder().id(1L).viv(BigDecimal.valueOf(50)).build())).build();
        Payment payment2 = Payment.builder()
                                  .id(1L)
                                  .date(LocalDateTime.of(2020, Month.JULY, 1, 12, 0))
                                  .receiver(testUser)
                                  .actions(List.of(Action.builder().id(2L).viv(BigDecimal.valueOf(50)).build())).build();
        when(paymentRepository.findAllByReceiverOrderByDateDesc(testUser)).thenReturn(List.of(payment2, payment1));
        PaymentService paymentService = new PaymentService(userRepository, paymentRepository);

        //When
        List<PaymentDto> paymentDtos = paymentService.findAllByReceiver(testUser.getId());

        //Then
        verify(userRepository).findById(testUser.getId());
        verify(paymentRepository).findAllByReceiverOrderByDateDesc(testUser);
        PaymentDto expectedPaymentDto1 = PaymentDto.builder()
                                                   .id(payment1.getId())
                                                   .userId(testUser.getId())
                                                   .date(payment1.getDate())
                                                   .viv(BigDecimal.valueOf(50))
                                                   .amount(BigDecimal.valueOf(250)).build();
        PaymentDto expectedPaymentDto2 = PaymentDto.builder()
                                                   .id(payment2.getId())
                                                   .userId(testUser.getId())
                                                   .date(payment2.getDate())
                                                   .viv(BigDecimal.valueOf(50))
                                                   .amount(BigDecimal.valueOf(250)).build();
        assertThat(paymentDtos).containsExactly(expectedPaymentDto2, expectedPaymentDto1);
    }
}
