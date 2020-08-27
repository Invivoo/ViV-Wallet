import { Role } from "../models/role";
import jwt_decode from "jwt-decode";
import { getToken, login } from "x4b-ui";

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

export class LoginService {
    private token: string | undefined;
    private decodedToken: DecodedJwtTokenContent;
    private authorizations: Authorizations;

    constructor() {
        this.token = process.env.NODE_ENV === "production" ? getToken() : process.env.VUE_APP_DEV_JWT;
        this.decodedToken = this.token && jwt_decode<DecodedJwtTokenContent>(this.token);
        this.authorizations = JSON.parse(this.decodedToken["viv-wallet"]) as Authorizations;
    }

    getRoles(): Role[] {
        return this.authorizations.roles;
    }

    getUserId(): string {
        return this.authorizations.userId;
    }

    getUserFullName(): string {
        return this.decodedToken.user;
    }

    getJwtToken(): string | undefined {
        return this.token;
    }

    ensureLoggedIn() {
        if (!this.token) {
            login(process.env.VUE_APP_LOGIN_URL);
        }
    }
}
