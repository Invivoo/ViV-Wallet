import { ConsultantStatus } from "./consultant";
import { Expertise } from "./expertise";

export interface User {
    id?: string;
    user: string;
    fullName: string;
    email: string;
    expertise?: Expertise;
    status?: ConsultantStatus;
}
