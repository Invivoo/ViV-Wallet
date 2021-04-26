// eslint-disable-next-line camelcase
import jwt_decode from "jwt-decode";
import { getToken } from "x4b-ui";
import { Role } from "../models/role";

export interface DecodedJwtTokenContent {
    exp: number;
    iss: string;
    user: string;
    // eslint-disable-next-line sonarjs/no-duplicate-string
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
        const token = process.env.VUE_APP_DEV_JWT || getCurrentToken();
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

    static getJwtToken(): string {
        return getCurrentToken();
    }
}
