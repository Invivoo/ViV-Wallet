export interface Payment {
    id: string;
    date: Date;
    viv: number;
    amount: number;
}
export interface PaymentPost {
    date: Date;
    receiverId: string;
    actionIds: string[];
}
