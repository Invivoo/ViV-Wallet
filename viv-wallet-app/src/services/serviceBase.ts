import axios, { AxiosInstance } from "axios";
import getConfigValue from "@/utils/configUtils";
import { LoginService } from "./login";

export class ServiceBase {
    protected http: AxiosInstance;

    protected loginService: LoginService;

    constructor(http?: AxiosInstance, route = "") {
        this.loginService = new LoginService();
        this.http =
            http ||
            axios.create({
                baseURL: `${getConfigValue("VUE_APP_BACKEND_BASE_URL")}${route}`,
                timeout: undefined,
                headers: { Authorization: `Bearer ${LoginService.getJwtToken()}` },
                withCredentials: true,
            });
    }
}
