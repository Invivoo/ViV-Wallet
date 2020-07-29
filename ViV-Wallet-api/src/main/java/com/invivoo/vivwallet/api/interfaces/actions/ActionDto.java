package com.invivoo.vivwallet.api.interfaces.actions;

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
public class ActionDto {

    private Long id;
    private Long userId;
    private String type;
    private String comment;
    private LocalDateTime creationDate;
    private BigDecimal payment;
    private String status;
    private LocalDateTime paymentDate;
    private String expertise;
}
