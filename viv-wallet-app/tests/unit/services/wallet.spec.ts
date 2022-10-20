import axios from "axios";
import { PaymentStatus } from "@/models/action";
import { Payment, PaymentPost } from "@/models/payment";
import { RawAction, WalletService } from "@/services/wallet";

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
            data: { value: balance },
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedBalance = await service.getUserBalance("id1");
        expect(returnedBalance).toEqual(balance);

        expect(mockedAxios.get).toHaveBeenCalledWith(`/users/id1/balance`);
    });

    it("should get the actions of a given user", async () => {
        const action: RawAction = {
            id: "id1",
            type: "Interview",
            comment: "This is a comment",
            creationDate: "2020-07-15T13:00:00.000Z",
            valueDate: "2020-07-15T13:00:00.000Z",
            payment: 20,
            status: "Paid",
            paymentDate: "2020-07-15T13:00:00.000Z",
            expertise: "Front-End",
        };
        const response = {
            data: [action],
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedActions = await service.getUserActions("id1");
        expect(1).toEqual(returnedActions.length);
        expect(returnedActions[0]).toMatchObject({
            ...action,
            status: PaymentStatus.Paid,
            paymentDate: new Date(action.paymentDate),
            creationDate: new Date(action.creationDate),
            valueDate: new Date(action.valueDate),
        });

        expect(mockedAxios.get).toHaveBeenCalledWith(`/users/id1/actions`);
    });

    it("should get only unpaid actions of a given user", async () => {
        const actions = [
            {
                id: "id1",
                type: "Interview",
                payment: 20,
                valueDate: new Date("2020-07-15T15:00:00"),
                status: PaymentStatus[PaymentStatus.Unpaid],
                expertise: "Front-End",
            },
            {
                id: "id1",
                type: "Interview",
                payment: 30,
                valueDate: new Date("2020-07-15T15:00:00"),
                status: PaymentStatus[PaymentStatus.Paid],
                expertise: "Front-End",
            },
            {
                id: "id1",
                type: "Interview",
                payment: 50,
                valueDate: new Date("2020-07-15T15:00:00"),
                status: PaymentStatus[PaymentStatus.Unpaid],
                expertise: "Front-End",
            },
        ];
        const response = {
            data: actions,
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedActions = await service.getUserPayableActions("id1");

        expect(2).toEqual(returnedActions.length);
        returnedActions.forEach((action) => {
            expect(PaymentStatus.Unpaid).toEqual(action.status);
        });

        expect(mockedAxios.get).toHaveBeenCalledWith(`/users/id1/actions`);
    });

    it("should get the payments of a given user", async () => {
        const payment: Payment = {
            id: "id1",
            date: new Date(),
            viv: 1000,
            amount: 2200,
        };
        const response = {
            data: [payment],
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedPayments = await service.getUserPayments("id1");
        expect(1).toEqual(returnedPayments.length);
        expect(payment).toEqual(returnedPayments[0]);

        expect(mockedAxios.get).toHaveBeenCalledWith(`/users/id1/payments`);
    });

    it("should post payment of a given user", async () => {
        const postedPayment: PaymentPost = {
            receiverId: "userId",
            date: new Date(2020, 1, 1),
            vivAmount: 200,
        };
        const result = true;
        const response = {
            data: result,
        };

        mockedAxios.post.mockReturnValue(Promise.resolve(response));

        expect(await service.saveUserPayment(postedPayment)).toEqual(result);

        expect(mockedAxios.post).toHaveBeenCalledWith(`payments`, postedPayment);
    });
});
