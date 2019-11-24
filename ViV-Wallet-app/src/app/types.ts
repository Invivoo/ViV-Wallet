export interface User {
    id: string;
    login: string;
    name: string;
    email: string;
}

export enum Role {
    ExpertiseConsultant,
    ExpertiseManager,
    TeamMember,
    TeamMAnager,
    Admin
}
