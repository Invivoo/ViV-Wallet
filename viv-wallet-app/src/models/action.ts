export enum ValidationStatus {
    Rejected,
    Done
}

export interface Action {
    id: string;
    type: string;
    comment: string;
    creationDate: Date;
    payment: number;
    status: ValidationStatus;
    validationDate: Date;
    expertise: string;
}
