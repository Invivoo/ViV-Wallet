package com.invivoo.vivwallet.api.interfaces.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private LocalDate date;
    private Long receiverId;
    private int vivAmount;
}
