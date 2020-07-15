import axios, { AxiosInstance } from "axios";
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "@/config/constants";
import { getToken } from "x4b-ui";
import { ServiceBase } from "./serviceBase";

export class BalanceService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http);
    }

    async getBalance(userId: string): Promise<number> {
        return (await this.http.get<{ value: number }>(`${userId}/balance`)).data.value;
    }
}
