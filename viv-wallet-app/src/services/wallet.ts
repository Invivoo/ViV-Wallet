import {AxiosInstance} from 'axios';
import {ServiceBase} from './serviceBase';
import {Action, PaymentStatus} from '@/models/action';
import {Payment, PaymentPost} from '@/models/payment';

export class WalletService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http, "");
    }

    async getAllActions(): Promise<Action[]> {
        const rawData = (await this.http.get(`/actions`)).data;
        return rawData.map((action) => {
            action.creationDate = new Date(action.creationDate);
            action.paymentDate = new Date(action.paymentDate);
            action.status = PaymentStatus[action.status];
            return action;
        });
    }

    async getUserBalance(userId: string): Promise<number> {
        return (await this.http.get<{ value: number }>(`/users/${userId}/balance`)).data.value;
    }

    async getUserActions(userId: string): Promise<Action[]> {
        const rawData = (await this.http.get(`/users/${userId}/actions`)).data;
        return rawData.map((action) => {
            action.creationDate = new Date(action.creationDate);
            action.valueDate = new Date(action.valueDate);
            action.paymentDate = new Date(action.paymentDate);
            action.status = PaymentStatus[action.status];
            return action;
        });
    }

    async getUserPayableActions(userId: string): Promise<Action[]> {
        const now = new Date();
        const actions = await this.getUserActions(userId);
        return actions.filter(
            (action) =>
                action.status === PaymentStatus.Unpaid &&
                (action.valueDate.getFullYear() < now.getFullYear() ||
                    (action.valueDate.getFullYear() === now.getFullYear() &&
                        action.valueDate.getMonth() < now.getMonth()))
        );
    }

    async getUserPayments(userId: string): Promise<Payment[]> {
        const rawData = (await this.http.get(`/users/${userId}/payments`)).data;
        return rawData.map((action) => {
            action.date = new Date(action.date);
            return action;
        });
    }

    async saveUserPayment(payment: PaymentPost): Promise<Object> {
        return (await this.http.post<boolean>("payments", payment)).data;
    }
}
