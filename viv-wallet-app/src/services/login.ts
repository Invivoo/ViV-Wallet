import { Role } from "../models/role";
import jwt_decode from "jwt-decode";
import { getToken, login } from "x4b-ui";
import { LOGIN_URL } from "../config/constants";

export interface DecodedJwtTokenContent {
    exp: number;
    iss: string;
    user: string;
    // for now we use scenario tokens... but hopefully this will change soon
    scenario: string;
}

export class LoginService {
    private token: string | undefined;
    private decodedToken: DecodedJwtTokenContent | undefined;

    constructor() {
        this.token = getToken();
        this.decodedToken = this.token && jwt_decode<DecodedJwtTokenContent>(this.token);
    }

    getCurrentRole(): Role | undefined {
        return Role.Admin;
    }

    getJwtToken(): string | undefined {
        return this.token;
    }

    ensureLoggedIn() {
        if (!this.token) {
            login(LOGIN_URL);
        }
    }
}
