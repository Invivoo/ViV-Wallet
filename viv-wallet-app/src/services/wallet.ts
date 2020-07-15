import { AxiosInstance } from "axios";
import { ServiceBase } from "./serviceBase";
import { Action, ValidationStatus } from "@/models/action";

export class WalletService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http);
    }

    async getBalance(userId: string): Promise<number> {
        return (await this.http.get<{ value: number }>(`${userId}/balance`)).data.value;
    }

    async getActions(userId: string): Promise<Action[]> {
        const rawData = (await this.http.get(`${userId}/actions`)).data;
        return rawData.map(action => {
            action.creationDate = new Date(action.creationDate);
            action.validationDate = new Date(action.validationDate);
            action.status = ValidationStatus[action.status];
            return action;
        });
    }
}
