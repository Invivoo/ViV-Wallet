import axios from "axios";
import { WalletService } from "@/services/wallet";
import { Action, PaymentStatus } from "@/models/action";
import { Payment } from "@/models/payment";

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("BalanceService", () => {
    let service: WalletService;

    beforeEach(() => {
        mockedAxios.get.mockClear();
        mockedAxios.get.mockReturnValue(Promise.resolve({}));

        service = new WalletService(mockedAxios);
    });

    it("should be created", () => {
        expect(service).toBeTruthy();
    });

    it("should get the viv balance of a given user", async () => {
        const balance = 200;
        const response = {
            data: { value: balance }
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedBalance = await service.getBalance("id1");
        expect(returnedBalance).toEqual(balance);

        expect(mockedAxios.get).toHaveBeenCalledWith(`id1/balance`);
    });

    it("should get the actions of a given user", async () => {
        const action: Action = {
            id: "id1",
            type: "Interview",
            comment: "This is a comment",
            creationDate: new Date("2020-07-15T15:00:00"),
            payment: 20,
            status: PaymentStatus.Paid,
            paymentDate: new Date("2020-07-15T15:00:00"),
            expertise: "Front-End"
        };
        const response = {
            data: [action]
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedActions = await service.getActions("id1");
        expect(1).toEqual(returnedActions.length);
        expect(action).toEqual(returnedActions[0]);

        expect(mockedAxios.get).toHaveBeenCalledWith(`id1/actions`);
    });

    it("should get the payments of a given user", async () => {
        const payment: Payment = {
            id: "id1",
            date: new Date(),
            viv: 1000,
            amount: 2200
        };
        const response = {
            data: [payment]
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedPayments = await service.getPayments("id1");
        expect(1).toEqual(returnedPayments.length);
        expect(payment).toEqual(returnedPayments[0]);

        expect(mockedAxios.get).toHaveBeenCalledWith(`id1/payments`);
    });

    it("should post payment of a given user", async () => {

        const userId= "userId";
        const payment: Payment = {
            id: "id1",
            date: new Date(),
            viv: 1000,
            amount: 2200
        };

        const response = {
            data: payment
        };
        mockedAxios.post.mockReturnValue(Promise.resolve(response));

        const postedPayment = { ...payment, id: "id1" };
        expect(await service.savePayment(userId, postedPayment)).toEqual(payment);

        expect(mockedAxios.post).toHaveBeenCalledWith(`${userId}/payments`, postedPayment);
    });
    
});
