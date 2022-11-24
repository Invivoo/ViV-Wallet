package com.invivoo.vivwallet.api.interfaces.payments;

import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        Optional<User> connectedUser = securityService.getConnectedUser();
        if (receiverOpt.isEmpty() || connectedUser.isEmpty()) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        User receiver = receiverOpt.get();
        Payment payment = Payment.builder()
                                 .date(paymentRequest.getDate())
                                 .receiver(receiver)
                                 .creator(connectedUser.get())
                                 .vivAmount(paymentRequest.getVivAmount())
                                 .build();
        Payment savedPayment = paymentService.save(payment);
        return ResponseEntity.created(getLocation(savedPayment))
                             .body(PaymentDto.createFromPayment(savedPayment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMINISTRATOR')")
    public ResponseEntity<PaymentDto> deletePayment(@PathVariable("id") Long paymentId) {
        return paymentService.findById(paymentId)
                             .map(paymentService::delete)
                             .map(PaymentDto::createFromPayment)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound()
                                                   .build());
    }

    private URI getLocation(Payment payment) {
        return URI.create(String.format("%s/%s", API_V_1_PAYMENTS, payment.getId()));
    }

}
