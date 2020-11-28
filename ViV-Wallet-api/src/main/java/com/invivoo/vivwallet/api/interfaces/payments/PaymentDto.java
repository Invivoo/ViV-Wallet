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
    private BigDecimal viv;
    private BigDecimal amount;

    public static PaymentDto createFromPayment(Payment payment) {
        int totalViv = payment.getActions()
                              .stream()
                              .mapToInt(action -> action.getViv().intValue())
                              .sum();
        return PaymentDto.builder()
                         .id(payment.getId())
                         .userId(payment.getReceiver().getId())
                         .date(payment.getDate())
                         .viv(BigDecimal.valueOf(totalViv))
                         .amount(BigDecimal.valueOf(totalViv * FACTOR_VIV_TO_AMOUNT)).build();
    }

}
