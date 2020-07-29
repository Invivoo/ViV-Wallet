package com.invivoo.vivwallet.api.interfaces.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long id;
    private Long userId;
    private LocalDateTime date;
    private BigDecimal viv;
    private BigDecimal amount;
}
