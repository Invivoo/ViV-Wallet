package com.invivoo.vivwallet.api.interfaces.payments;

import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(PaymentsController.API_V_1_PAYMENTS)
@AllArgsConstructor
public class PaymentsController {

    public static final String API_V_1_PAYMENTS = "/api/v1/payments";

    private final PaymentService paymentService;
    private final ActionService actionService;
    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getPayments() {
        return ResponseEntity.ok(paymentService.getAll()
                                               .stream()
                                               .map(PaymentDto::createFromPayment)
                                               .collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMINISTRATOR')")
    public ResponseEntity<PaymentDto> postPayment(@RequestBody PaymentRequest paymentRequest) {
        Optional<User> receiverOpt = userService.findById(paymentRequest.getReceiverId());
        List<Action> actionsToPay = actionService.findAllById(paymentRequest.getActionIds())
                                                 .stream()
                                                 .filter(action -> action.getVivAmount() > 0)
                                                 .filter(Action::isPayable)
                                                 .collect(Collectors.toList());
        Optional<User> connectedUser = securityService.getConnectedUser();
        if (receiverOpt.isEmpty() || connectedUser.isEmpty()) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        User receiver = receiverOpt.get();
        int balance = userService.computeBalance(receiver);
        if(paymentRequest.getVivAmount() > balance) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        Payment payment = Payment.builder()
                                     .date(paymentRequest.getDate())
                                     .receiver(receiver)
                                     .creator(connectedUser.get())
                                     .vivAmount(paymentRequest.getVivAmount())
                                     .actions(actionsToPay)
                                     .build();
        Payment savedPayment = paymentService.save(payment);
        return ResponseEntity.created(getLocation(savedPayment))
                             .body(PaymentDto.createFromPayment(savedPayment));
    }

    @GetMapping("/{paymentId}/actions")
    public ResponseEntity<List<Action>> getPaymentActions(@PathVariable("paymentId") Long paymentId) {
        List<Action> actions = paymentService.getActionsByPaymentId(paymentId);
        return ResponseEntity.ok(actions);
    }

    private URI getLocation(Payment payment) {
        return URI.create(String.format("%s/%s", API_V_1_PAYMENTS, payment.getId()));
    }

}
