import "@testing-library/jest-dom";
import faker from "faker";
import { rest } from "msw";
import { setupServer } from "msw/node";
import Payment from "@/views/Payment.vue";
import { render, screen, waitForElementToBeRemoved, within } from "../testHelpers";

const server = setupServer();

function setupRestMocks(
    userId: string,
    fullName: string,
    balance: number,
    actions: Array<{ payment: number; id: number; status: string }>
) {
    const user = faker.internet.userName();
    const email = faker.internet.email();
    const actionResponse = actions.map((action) => ({
        userId,
        id: action.id,
        type: "Interview",
        comment: "This is a comment",
        creationDate: faker.date.past(),
        valueDate: faker.date.past(),
        payment: action.payment,
        status: action.status,
        ...(action.status === "Paid" && { paymentDate: faker.date.past() }),
        expertise: "Front-End",
        achiever: {
            id: userId,
            user,
            fullName,
            expertise: "csharp",
            email,
            status: "CONSULTANT_SENIOR",
            startDate: faker.date.past(),
        },
    }));
    server.use(
        rest.get(`${process.env.VUE_APP_BACKEND_BASE_URL}/users/${userId}`, (_, res, ctx) => {
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
        rest.get(`${process.env.VUE_APP_BACKEND_BASE_URL}/users/${userId}/balance`, (_, res, ctx) => {
            return res(
                ctx.status(200),
                ctx.json({
                    value: balance,
                })
            );
        }),
        rest.get(`${process.env.VUE_APP_BACKEND_BASE_URL}/users/${userId}/actions`, (_, res, ctx) => {
            return res(ctx.status(200), ctx.json(actionResponse));
        })
    );
}

describe("Payment", () => {
    beforeAll(() => server.listen());
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    it("should display the list of unpaid actions and the viv balance", async () => {
        const userId = faker.datatype.number().toString();
        const fullName = faker.name.findName();
        const balance = faker.datatype.number(1000);
        const unPaidActions = [
            { id: faker.unique(faker.datatype.number), payment: faker.datatype.number(50), status: "Unpaid" },
            { id: faker.unique(faker.datatype.number), payment: faker.datatype.number(50), status: "Unpaid" },
        ];
        setupRestMocks(userId, fullName, balance, [
            ...unPaidActions,
            { id: faker.unique(faker.datatype.number), payment: faker.datatype.number(50), status: "Paid" },
        ]);
        await render(Payment, { props: { id: userId } });
        await waitForElementToBeRemoved(() => screen.getByText(/chargement/i));

        expect(screen.getByText(fullName)).toBeInTheDocument();
        expect(screen.getByText(`${balance} VIV`)).toBeInTheDocument();

        expect(screen.getByLabelText(/total vivs/i)).toHaveValue("0");
        expect(screen.getByLabelText(/montant/i)).toHaveValue("0 €");

        const actionTable = within(screen.getByRole("table", { name: /liste des actions/i }));
        expect(actionTable.getAllByRole("row").length).toBe(unPaidActions.length + 1); // + the header
        unPaidActions.forEach((action) => {
            expect(actionTable.getByTestId(action.id)).toBeInTheDocument();
        });
    });
});
