import { Expertise } from "@/models/expertise";

export interface Consultant {
    id?: string;
    user?: string;
    email?: string;
    fullName?: string;
    expertise?: Expertise;
    status?: ConsultantStatus;
    startDate?: string;
    endDate?: string;
    roles?: string[];
}

export enum ConsultantStatus {
    CONSULTANT_SENIOR = "CONSULTANT_SENIOR",
    CONSULTANT_SENIOR_IN_ONBOARDING = "CONSULTANT_SENIOR_IN_ONBOARDING",
    MANAGER = "MANAGER",
    MANAGER_SENIOR = "MANAGER_SENIOR",
}

export function toString(status: ConsultantStatus): string {
    switch (status) {
        case ConsultantStatus.CONSULTANT_SENIOR:
            return "Senior";
        case ConsultantStatus.MANAGER:
            return "Manager";
        case ConsultantStatus.MANAGER_SENIOR:
            return "Manager Senior";
        case ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING:
            return "Onboarding";
        default:
            return "Inconnu";
    }
}
