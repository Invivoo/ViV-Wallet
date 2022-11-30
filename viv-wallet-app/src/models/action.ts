import { Consultant } from "@/models/consultant";

export enum PaymentStatus {
    PAYABLE = "PAYABLE",
    NON_PAYABLE = "NON_PAYABLE",
    TO_VALIDATE = "TO_VALIDATE",
}

export interface Action {
    id: string;
    type: string;
    comment?: string;
    creationDate: Date;
    valueDate: Date;
    payment: number;
    status: PaymentStatus;
    paymentDate?: Date;
    expertise: string;
}

export interface ActionForHistory extends Action {
    achiever?: Consultant;
}
