package com.invivoo.vivwallet.api.interfaces.payments;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PaymentsController.API_V_1_PAYMENTS)
@AllArgsConstructor
public class PaymentsController {

    public static final String API_V_1_PAYMENTS = "/api/v1/payments";

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}/actions")
    public ResponseEntity<List<Action>> getActionsByPaymentId(@PathVariable("paymentId") Long paymentId) {
        List<Action> actions = paymentService.getActionsByPaymentId(paymentId);
        return ResponseEntity.ok(actions);
    }

}
