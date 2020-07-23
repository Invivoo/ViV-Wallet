package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    public static final int FACTOR_VIV_TO_AMOUNT = 5;

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentDto> findAllByReceiver(Long receiverId) {
        List<PaymentDto> payments = new ArrayList<>();

        userRepository.findById(receiverId).ifPresent(
                user -> payments.addAll(paymentRepository.findAllByReceiverOrderByDateDesc(user.getFullName()).stream()
                            .map(payment -> convertToDto(payment, receiverId))
                            .collect(Collectors.toList()))
        );

        return payments;
    }

    private PaymentDto convertToDto(Payment payment, Long userId) {
        int totalViv = payment.getActions().stream().mapToInt(action -> action.getViv().intValue()).sum();
        return PaymentDto.builder()
                .id(payment.getId())
                .userId(userId)
                .date(payment.getDate())
                .viv(BigDecimal.valueOf(totalViv))
                .amount(BigDecimal.valueOf(totalViv* FACTOR_VIV_TO_AMOUNT)).build();
    }
}
