package com.invivoo.vivwallet.api.domain.expertise;

public enum UserExpertiseStatus {
    CONSULTANT_SENIOR_IN_ONBOARDING("Consultant Senior en cours d'onboarding"),
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
}
