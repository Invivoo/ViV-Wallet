export interface Consultant {
    id: string;
    user: string;
    email: string;
    fullname: string;
    status: ConsultantStatus;
}

export enum ConsultantStatus {
    SENIOR,
    SENIOR_IN_ONBOARDING,
    MANAGER
}
