package com.invivoo.vivwallet.api.interfaces.payments;

import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
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
import java.util.Collection;
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
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMINISTRATOR')")
    public ResponseEntity<Payment> postPayment(@RequestBody PaymentRequest paymentRequest) {
        Optional<User> receiver = userService.findById(paymentRequest.getReceiverId());
        List<Action> actions = actionService.findAllById(paymentRequest.getActionIds());
        Optional<User> connectedUser = securityService.getConnectedUser();
        if (receiver.isEmpty() || actions.isEmpty() || connectedUser.isEmpty()) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        Payment payment = Payment.builder()
                                 .date(paymentRequest.getDate())
                                 .receiver(receiver.get())
                                 .creator(connectedUser.get())
                                 .actions(actions)
                                 .build();
        Payment savedPayment = paymentService.save(payment);
        return ResponseEntity.created(getLocation(savedPayment))
                             .body(savedPayment);
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
