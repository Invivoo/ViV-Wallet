package com.invivoo.vivwallet.api.domain.expertise;

public enum ExpertiseMemberStatus {
    CONSULTANT_SENIOR_IN_ONBOARDING("Consultant Senior en cours d'onboarding"),
    CONSULTANT_SENIOR("Consultant Senior"),
    MANAGER("Manager"),
    MANAGER_SENIOR("Manager Senior");

    private String expertiseMemberStatusName;

    private ExpertiseMemberStatus(String expertiseMemberStatusName) {
        this.expertiseMemberStatusName = expertiseMemberStatusName;
    }

    public String getExpertiseMemberStatusName() {
        return this.expertiseMemberStatusName;
    }
}
