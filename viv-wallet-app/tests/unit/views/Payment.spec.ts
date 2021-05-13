import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";
import faker from "faker";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { PaymentPost } from "@/models/payment";
import Payment from "@/views/Payment.vue";
import { render, screen, waitFor, waitForElementToBeRemoved, within } from "../testHelpers";

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

function generateTestData(initialVivs: number) {
    const userId = faker.datatype.number().toString();
    const fullName = faker.name.findName();
    const unPaidActions = [
        { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Unpaid" },
        { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Unpaid" },
    ];
    const balance = initialVivs + unPaidActions.reduce((acc, action) => action.payment + acc, 0);
    return {
        userId,
        fullName,
        unPaidActions,
        initialVivs,
        balance,
    };
}

function getAmount() {
    return faker.datatype.number({ min: 1, max: 50 });
}

describe("Payment", () => {
    beforeAll(() => server.listen());
    afterEach(() => {
        server.resetHandlers();
        jest.clearAllMocks();
    });
    afterAll(() => server.close());

    it("should display the list of unpaid actions and the viv balance", async () => {
        const { userId, fullName, unPaidActions, balance } = generateTestData(0);
        setupRestMocks(userId, fullName, balance, [
            ...unPaidActions,
            { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Paid" },
        ]);
        await render(Payment, { props: { id: userId } });
        await waitForElementToBeRemoved(() => screen.getByText(/chargement/i));

        expect(screen.getByText(fullName)).toBeInTheDocument();
        expect(screen.getByText(`${balance} VIV`)).toBeInTheDocument();

        await waitFor(() => expect(screen.getByLabelText(/total vivs/i)).toHaveValue("0"));
        expect(screen.getByLabelText(/montant/i)).toHaveValue("0 €");
        expect(screen.queryByText(/dont .* de report/)).not.toBeInTheDocument();

        const actionTable = within(screen.getByRole("table", { name: /liste des actions/i }));
        expect(actionTable.getAllByRole("row").length).toBe(unPaidActions.length + 1); // + the header
        unPaidActions.forEach((action) => {
            expect(actionTable.getByTestId(action.id)).toBeInTheDocument();
        });
    });

    it("should display initial VIVs", async () => {
        const initialVivs = getAmount();
        const { userId, fullName, unPaidActions, balance } = generateTestData(initialVivs);
        setupRestMocks(userId, fullName, balance, [
            ...unPaidActions,
            { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Paid" },
        ]);
        await render(Payment, { props: { id: userId } });
        await waitForElementToBeRemoved(() => screen.getByText(/chargement/i));

        await waitFor(() => expect(screen.getByLabelText(/total vivs/i)).toHaveValue(initialVivs.toString()));
        expect(screen.getByLabelText(/montant/i)).toHaveValue(`${5 * initialVivs} €`);
        expect(screen.getByText(`(dont ${initialVivs} de report)`)).toBeInTheDocument();
    });

    it("should select an unpaid action and request a payment", async () => {
        const initialVivs = getAmount();
        const { userId, fullName, unPaidActions, balance } = generateTestData(initialVivs);
        server.use(
            rest.post(`${process.env.VUE_APP_BACKEND_BASE_URL}/payments`, (req, res, ctx) => {
                const body = req.body as PaymentPost;
                expect(body.actionIds).toHaveLength(1);
                expect(body.actionIds).toEqual(expect.arrayContaining([unPaidActions[0].id]));
                expect(body.vivAmount).toEqual(initialVivs + unPaidActions[0].payment);
                return res(ctx.status(200));
            })
        );
        setupRestMocks(userId, fullName, balance, [
            ...unPaidActions,
            { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Paid" },
        ]);
        const { router } = await render(Payment, { props: { id: userId } });
        await waitForElementToBeRemoved(() => screen.getByText(/chargement/i));

        await waitFor(() => expect(screen.getByLabelText(/total vivs/i)).toHaveValue(initialVivs.toString()));
        expect(screen.getByLabelText(/montant/i)).toHaveValue(`${5 * initialVivs} €`);
        expect(screen.getByText(`(dont ${initialVivs} de report)`)).toBeInTheDocument();

        // Select an unpaid action
        const actionCheckboxes = screen.getAllByRole("checkbox", { name: /sélectionner l'action du/i });
        userEvent.click(actionCheckboxes[0]);
        expect(actionCheckboxes[0]).toBeChecked();

        const expectedVivs = unPaidActions[0].payment + initialVivs;
        await waitFor(() => expect(screen.getByLabelText(/total vivs/i)).toHaveValue(expectedVivs.toString()));
        expect(screen.getByLabelText(/montant/i)).toHaveValue(`${5 * expectedVivs} €`);

        // Request a payment
        router.push = jest.fn();
        userEvent.click(screen.getByRole("button", { name: /valider/i }));

        await waitFor(() =>
            expect(router.push).toHaveBeenCalledWith(expect.objectContaining({ path: `/wallets/${userId}` }))
        );
    });

    it("should select / unselect all unpaid actions", async () => {
        const initialVivs = getAmount();
        const { userId, fullName, unPaidActions, balance } = generateTestData(initialVivs);
        setupRestMocks(userId, fullName, balance, [
            ...unPaidActions,
            { id: faker.unique(faker.datatype.number), payment: getAmount(), status: "Paid" },
        ]);
        await render(Payment, { props: { id: userId } });
        await waitForElementToBeRemoved(() => screen.getByText(/chargement/i));

        await waitFor(() => expect(screen.getByLabelText(/total vivs/i)).toHaveValue(initialVivs.toString()));

        // Select all unpaid actions
        const selectAllChecbox = screen.getByRole("checkbox", { name: /tout sélectionner/i });
        userEvent.click(selectAllChecbox);
        expect(selectAllChecbox).toBeChecked();
        const actionCheckboxes = screen.getAllByRole("checkbox", { name: /sélectionner l'action du/i });
        await waitFor(() => {
            actionCheckboxes.forEach((actionCheckbox) => {
                expect(actionCheckbox).toBeChecked();
            });
        });

        // Unselect all unpaid actions
        userEvent.click(selectAllChecbox);
        expect(selectAllChecbox).not.toBeChecked();
        await waitFor(() => {
            actionCheckboxes.forEach((actionCheckbox) => {
                expect(actionCheckbox).not.toBeChecked();
            });
        });
    });
});
