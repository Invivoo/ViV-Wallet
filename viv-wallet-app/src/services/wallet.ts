import { AxiosInstance } from "axios";
import { ServiceBase } from "./serviceBase";
import { Action, PaymentStatus } from "@/models/action";
import { Payment, PaymentPost } from "@/models/payment";

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
            action.paymentDate = new Date(action.paymentDate);
            action.status = PaymentStatus[action.status];
            return action;
        });
    }

    async getUnpaidActions(userId: string): Promise<Action[]> {
        const rawData = (await this.http.get(`${userId}/actions`)).data;
        return rawData.map(action => {
            action.creationDate = new Date(action.creationDate);
            action.paymentDate = new Date(action.paymentDate);
            action.status = PaymentStatus[action.status];
            return action;
        }).filter(action => action.status === PaymentStatus.Unpaid);
    }

    async getPayments(userId: string): Promise<Payment[]> {
        const rawData = (await this.http.get(`${userId}/payments`)).data;
        return rawData.map(action => {
            action.date = new Date(action.date);
            return action;
        });
    }

    async savePayment(payment: PaymentPost): Promise<Object> {
        return (await this.http.post<boolean>("payments", payment)).data;
    }
}
