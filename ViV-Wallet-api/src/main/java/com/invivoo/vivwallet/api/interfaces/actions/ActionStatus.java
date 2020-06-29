package com.invivoo.vivwallet.api.interfaces.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionStatus {
    UNPAID("Unpaid"),
    PAID("Paid");

    private final String label;
}
