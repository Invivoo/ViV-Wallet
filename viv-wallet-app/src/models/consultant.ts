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
