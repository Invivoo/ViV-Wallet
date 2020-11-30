import { ConsultantStatus } from "./consultant";
import { Expertise } from "./expertise";

export interface User {
    id?: string;
    user: string;
    fullName: string;
    email: string;
    expertiseDto: Expertise;
    status?: ConsultantStatus;
}
