import { Role } from "../models/role";
import jwt_decode from "jwt-decode";
import { getToken } from "x4b-ui";

export interface DecodedJwtTokenContent {
    exp: number;
    iss: string;
    user: string;
    "viv-wallet": string;
}

export interface Authorizations {
    roles: Role[];
    userId: string;
}

const getCurrentToken = () => {
    return getToken();
};

export class LoginService {
    private decodedToken?: DecodedJwtTokenContent;
    private authorizations: Authorizations;

    constructor() {
        const token = getCurrentToken();
        this.decodedToken = (token && jwt_decode<DecodedJwtTokenContent>(token)) || undefined;
        this.authorizations = (this.decodedToken &&
            this.decodedToken["viv-wallet"] &&
            JSON.parse(this.decodedToken["viv-wallet"])) as Authorizations;
    }

    getRoles(): Role[] {
        return this.authorizations.roles;
    }

    getUserId(): string {
        return this.authorizations.userId;
    }

    getUserFullName(): string {
        return this.decodedToken?.user || "";
    }

    getJwtToken(): string {
        return getCurrentToken();
    }
}
