package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {

    public static final int FACTOR_VIV_TO_AMOUNT = 5;

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ActionRepository actionRepository;

    public List<Payment> getAll() {
        List<Payment> payments = paymentRepository.getAllByOrderByDateDesc();
        return payments != null && !payments.isEmpty() ? payments : Collections.emptyList();
    }

    public List<Action> getActionsByPaymentId(Long paymentId) {
        List<Action> actions = actionRepository.findAllByPaymentIdOrderByDateDesc(paymentId);
        return actions != null && !actions.isEmpty() ? actions : Collections.emptyList();
    }

    public List<PaymentDto> findAllByReceiver(Long receiverId) {
        return userRepository.findById(receiverId)
                .map(paymentRepository::findAllByReceiverOrderByDateDesc)
                .map(payments -> payments.stream()
                        .map(payment -> convertToDto(payment, receiverId))
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    private PaymentDto convertToDto(Payment payment, Long userId) {
        int totalViv = payment.getActions().stream().mapToInt(action -> action.getViv().intValue()).sum();
        return PaymentDto.builder()
                .id(payment.getId())
                .userId(userId)
                .date(payment.getDate())
                .viv(BigDecimal.valueOf(totalViv))
                .amount(BigDecimal.valueOf(totalViv * FACTOR_VIV_TO_AMOUNT)).build();
    }
}
