package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ActionRepository actionRepository;

    public List<Payment> getAll() {
        List<Payment> payments = paymentRepository.getAllByOrderByDateDesc();
        return payments != null && !payments.isEmpty() ? payments : Collections.emptyList();
    }

    public List<Payment> findAllByReceiverOrderByDateAsc(User receiver) {
        return paymentRepository.findAllByReceiverOrderByDateAsc(receiver);
    }

    public List<Action> getActionsByPaymentId(Long paymentId) {
        List<Action> actions = actionRepository.findAllByPaymentIdOrderByDateDesc(paymentId);
        return actions != null && !actions.isEmpty() ? actions : Collections.emptyList();
    }

    public List<PaymentDto> findAllByReceiver(Long receiverId) {
        return userRepository.findById(receiverId)
                             .map(paymentRepository::findAllByReceiverOrderByDateDesc)
                             .map(payments -> payments.stream()
                                                      .map(PaymentDto::createFromPayment)
                                                      .collect(Collectors.toList()))
                             .orElse(List.of());
    }

    public Optional<Payment> findByDateAndReceiver(LocalDate paymentDate, User user) {
        return paymentRepository.findByDateAndReceiver(paymentDate, user);
    }

    public Payment save(Payment payment) {
        Optional.ofNullable(payment.getActions())
                .ifPresent(actions -> actions.forEach(a -> a.setPayment(payment)));
        paymentRepository.save(payment);
        return payment;
    }

    public List<Payment> saveAll(List<Payment> payments) {
        return paymentRepository.saveAll(payments);
    }

    public void deleteAll() {
        paymentRepository.deleteAll();
    }
}
