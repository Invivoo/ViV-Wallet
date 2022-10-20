import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";
import faker from "faker";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { PaymentPost } from "@/models/payment";
import getConfigValue from "@/utils/configUtils";
import Payment from "@/views/Payment.vue";
import { render, screen, waitFor } from "../testHelpers";

function getAmount() {
    return faker.datatype.number({ min: 1, max: 500 });
}

const userId = faker.datatype.number().toString();
const fullName = faker.name.findName();
const user = faker.internet.userName();
const email = faker.internet.email();
const balance = getAmount();

function getRequestHandlers() {
    return [
        rest.get(`${getConfigValue("VUE_APP_BACKEND_BASE_URL")}/users/${userId}`, (_, res, ctx) => {
            return res(
                ctx.status(200),
                ctx.json({
                    id: userId,
                    user,
                    fullName,
                    status: "CONSULTANT_SENIOR",
                    startDate: faker.date.past(),
                    expertise: {
                        id: "csharp",
                        expertiseName: "Programmation C#",
                    },
                    email,
                })
            );
        }),
        rest.get(`${getConfigValue("VUE_APP_BACKEND_BASE_URL")}/users/${userId}/balance`, (_, res, ctx) => {
            return res(
                ctx.status(200),
                ctx.json({
                    value: balance,
                })
            );
        }),
    ];
}

const server = setupServer(...getRequestHandlers());

describe("Payment", () => {
    beforeAll(() => server.listen());
    afterEach(() => {
        server.resetHandlers();
        jest.clearAllMocks();
    });
    afterAll(() => server.close());

    it("should display an total vivs input with the balance as initial value", async () => {
        await render(Payment, { props: { id: userId } });

        expect(await screen.findByText(fullName)).toBeInTheDocument();
        expect(screen.getByText(`${balance} VIV`)).toBeInTheDocument();

        expect(await screen.findByLabelText(/total vivs/i)).toHaveValue(`${balance}`);
        expect(screen.getByText(`${balance * 5} €`));
    });

    it("can request a payment", async () => {
        const vivToPay = getAmount();
        server.use(
            rest.post(`${getConfigValue("VUE_APP_BACKEND_BASE_URL")}/payments`, (req, res, ctx) => {
                const body = req.body as PaymentPost;
                expect(body.vivAmount).toEqual(vivToPay);
                return res(ctx.status(200));
            })
        );
        const { router } = await render(Payment, { props: { id: userId } });

        expect(await screen.findByText(fullName)).toBeInTheDocument();
        expect(screen.getByText(`${balance} VIV`)).toBeInTheDocument();
        const totalVivInput = screen.getByLabelText(/total vivs/i);
        userEvent.clear(totalVivInput);
        userEvent.type(totalVivInput, `${vivToPay}`);
        expect(await screen.findByText(`${vivToPay * 5} €`));

        // Request a payment
        router.push = jest.fn();
        userEvent.click(screen.getByRole("button", { name: /valider/i }));

        await waitFor(() =>
            expect(router.push).toHaveBeenCalledWith(expect.objectContaining({ path: `/wallets/${userId}` }))
        );
    });
});
