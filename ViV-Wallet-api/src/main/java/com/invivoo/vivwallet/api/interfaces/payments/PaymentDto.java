package com.invivoo.vivwallet.api.interfaces.payments;

import com.invivoo.vivwallet.api.domain.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    public static final int FACTOR_VIV_TO_AMOUNT = 5;

    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer viv;
    private BigDecimal amount;

    public static PaymentDto createFromPayment(Payment payment) {
        return PaymentDto.builder()
                         .id(payment.getId())
                         .userId(payment.getReceiver().getId())
                         .date(payment.getDate())
                         .viv(payment.getVivAmount())
                         .amount(BigDecimal.valueOf(payment.getVivAmount() * FACTOR_VIV_TO_AMOUNT)).build();
    }

}
