import axios, { AxiosInstance } from "axios";
import { LoginService } from "./login";

export class ServiceBase {
    protected http: AxiosInstance;

    protected loginService: LoginService;

    constructor(http?: AxiosInstance, route = "") {
        this.loginService = new LoginService();
        this.http =
            http ||
            axios.create({
                baseURL: `${process.env.VUE_APP_BACKEND_BASE_URL}${route}`,
                timeout: process.env.VUE_APP_REQUEST_TIMEOUT_MS
                    ? Number.parseInt(process.env.VUE_APP_REQUEST_TIMEOUT_MS, 10)
                    : undefined,
                headers: { Authorization: `Bearer ${LoginService.getJwtToken()}` },
                withCredentials: true,
            });
    }
}
