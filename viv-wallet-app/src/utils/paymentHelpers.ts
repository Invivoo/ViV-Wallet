import { ActionForHistory, PaymentStatus } from "@/models/action";

export function isPaymentPaid(action: ActionForHistory) {
    return action.status === PaymentStatus.Paid;
}
export function formatPaymentStatus(status: PaymentStatus) {
    switch (status) {
        case PaymentStatus.Paid:
            return "Débloqué";
        case PaymentStatus.Unpaid:
        default:
            return "Non débloqué";
    }
}
export function getPaymentStatusType(status: PaymentStatus) {
    switch (status) {
        case PaymentStatus.Paid:
            return "green";
        case PaymentStatus.Unpaid:
        default:
            return "red";
    }
}

export default {
    isPaymentPaid,
    formatPaymentStatus,
    getPaymentStatusType,
};
