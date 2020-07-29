import axios, { AxiosInstance } from "axios";
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "@/config/constants";
import { LoginService } from "./login";

export class ServiceBase {
    protected http: AxiosInstance;
    protected loginService: LoginService;

    constructor(http?: AxiosInstance, route = "/users") {
        this.loginService = new LoginService();
        this.http =
            http ||
            axios.create({
                baseURL: `${BACKEND_BASE_URL}${route}`,
                timeout: REQUEST_TIMEOUT_MS,
                headers: { Authorization: `Bearer ${this.loginService.getJwtToken()}` },
                withCredentials: true
            });
    }
}
