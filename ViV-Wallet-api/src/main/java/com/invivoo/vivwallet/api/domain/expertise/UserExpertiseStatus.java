package com.invivoo.vivwallet.api.domain.expertise;

import java.util.Optional;
import java.util.stream.Stream;

public enum UserExpertiseStatus {
    CONSULTANT_SENIOR_IN_ONBOARDING("Consultant Senior en cours d'onboarding"),
    CONSULTANT("Consultant"),
    CONSULTANT_SENIOR("Consultant Senior"),
    MANAGER("Manager"),
    MANAGER_SENIOR("Manager Senior");

    private String expertiseMemberStatusName;

    private UserExpertiseStatus(String expertiseMemberStatusName) {
        this.expertiseMemberStatusName = expertiseMemberStatusName;
    }

    public String getExpertiseMemberStatusName() {
        return this.expertiseMemberStatusName;
    }

    public static Optional<UserExpertiseStatus> forName(String name) {
        return Stream.of(UserExpertiseStatus.values())
                     .filter(e -> e.name().equals(name))
                     .findFirst();
    }
}
