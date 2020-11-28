package com.invivoo.vivwallet.api.domain.role;

import java.util.Optional;
import java.util.stream.Stream;

public enum RoleType {
    CONSULTANT,
    EXPERTISE_MANAGER,
    SENIOR_MANAGER,
    COMPANY_ADMINISTRATOR;

    public static Optional<RoleType> forName(String name) {
        return Stream.of(RoleType.values())
                     .filter(e -> e.name().equals(name))
                     .findFirst();
    }
}
