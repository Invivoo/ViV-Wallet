import { AxiosInstance } from "axios";
import { Action, PaymentStatus } from "@/models/action";
import { Payment, PaymentPost } from "@/models/payment";
import { ServiceBase } from "./serviceBase";

export type RawAction = Omit<Action, "creationDate" | "paymentDate" | "status" | "valueDate"> & {
    creationDate: string;
    paymentDate: string;
    valueDate: string;
    status: keyof typeof PaymentStatus;
};

export type RawPayment = Omit<Payment, "date"> & {
    date: string;
};

export class WalletService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http, "");
    }

    async getAllActions(): Promise<Action[]> {
        const rawData = (await this.http.get<RawAction[]>(`/actions`)).data;
        return rawData.map((rawAction) => ({
            ...rawAction,
            creationDate: new Date(rawAction.creationDate),
            paymentDate: new Date(rawAction.paymentDate),
            valueDate: new Date(rawAction.valueDate),
            status: PaymentStatus[rawAction.status],
        }));
    }

    async getUserBalance(userId: string): Promise<number> {
        return (await this.http.get<{ value: number }>(`/users/${userId}/balance`)).data.value;
    }

    async getUserActions(userId: string): Promise<Action[]> {
        const rawData = (await this.http.get<RawAction[]>(`/users/${userId}/actions`)).data;
        return rawData.map((rawAction) => ({
            ...rawAction,
            creationDate: new Date(rawAction.creationDate),
            valueDate: new Date(rawAction.valueDate),
            paymentDate: rawAction.paymentDate ? new Date(rawAction.paymentDate) : undefined,
            status: PaymentStatus[rawAction.status],
        }));
    }

    async getUserPayableActions(userId: string): Promise<Action[]> {
        const now = new Date();
        const actions = await this.getUserActions(userId);
        return actions.filter(
            (action) =>
                action.status === PaymentStatus.NON_PAYABLE &&
                (action.valueDate.getFullYear() < now.getFullYear() ||
                    (action.valueDate.getFullYear() === now.getFullYear() &&
                        action.valueDate.getMonth() < now.getMonth()))
        );
    }

    async getUserPayments(userId: string): Promise<Payment[]> {
        const rawData = (await this.http.get<RawPayment[]>(`/users/${userId}/payments`)).data;
        return rawData.map((rawPayment) => ({
            ...rawPayment,
            date: new Date(rawPayment.date),
        }));
    }

    async saveUserPayment(payment: PaymentPost): Promise<Object> {
        return (await this.http.post<boolean>("payments", payment)).data;
    }

    async deleteUserPayment(paymentId: string) {
        return this.http.delete<{}>(`payments/${paymentId}`);
    }

    async saveActions(userId: string, actions: Action[]): Promise<Object> {
        const rawActions = actions.map((e) => ({
            ...e,
            creationDate: e.creationDate.toISOString(),
            valueDate: e.valueDate.toISOString(),
            paymentDate: e.paymentDate ? e.paymentDate.toISOString : undefined,
            status: e.status.toString(),
        }));
        return (await this.http.patch<RawAction[]>(`/users/${userId}/actions`, { updates: rawActions })).data;
    }
}
