import { Role } from '../models/role';

export class LoginService {

    private role: Role | undefined;

    constructor() {
        this.role = Role.Admin;
    }

    getCurrentRole(): Role | undefined {
        return this.role;
    }

    getJwtToken(): string | undefined {
        return '';
    }

    logout() {

    }
}
