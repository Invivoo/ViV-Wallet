import "@testing-library/jest-dom";
import Home from "@/views/Home.vue";
import { render, screen } from "../testHelpers";

describe("Home", () => {
    it("renders the home page", async () => {
        await render(Home);
        const linkToWallet = screen.getByRole("link", { name: /accéder à mon wallet/i }) as HTMLLinkElement;
        expect(linkToWallet).toBeInTheDocument();
        expect(linkToWallet).toHaveAttribute("href", "/wallet");
    });
});
