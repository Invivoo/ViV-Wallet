import axios from "axios";
import { BalanceService } from "@/services/balance";

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("BalanceService", () => {
    let service: BalanceService;

    beforeEach(() => {
        mockedAxios.get.mockClear();
        mockedAxios.get.mockReturnValue(Promise.resolve({}));

        service = new BalanceService(mockedAxios);
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
});
