import { PaymentStatus } from "@/models/action";

export function formatPaymentStatus(status: PaymentStatus) {
    switch (status) {
        case PaymentStatus.PAYABLE:
            return "Monétisable";
        case PaymentStatus.NON_PAYABLE:
            return "Non monétisable";
        default:
            return "À valider";
    }
}
export function getPaymentStatusType(status: PaymentStatus) {
    switch (status) {
        case PaymentStatus.PAYABLE:
            return "green";
        case PaymentStatus.TO_VALIDATE:
            return "yellow";
        case PaymentStatus.NON_PAYABLE:
        default:
            return "red";
    }
}

export default {
    formatPaymentStatus,
    getPaymentStatusType,
};
