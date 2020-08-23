export interface Consultant {
    id: string;
    user: string;
    email: string;
    fullname: string;
    status: ConsultantStatus;
}

export enum ConsultantStatus {
    CONSULTANT_SENIOR,
    CONSULTANT_SENIOR_IN_ONBOARDING,
    MANAGER
}

export function toString(status: ConsultantStatus): string {
    switch (status) {
        case ConsultantStatus.CONSULTANT_SENIOR:
            return "Senior";
        case ConsultantStatus.MANAGER:
            return "Manager";
        case ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING:
            return "Onboarding";
        default:
            return "Inconnu";
    }
}
