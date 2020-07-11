import UserEdit from "@/views/UserEdit.vue";
import { UsersService } from "@/services/users";
import { factory } from "../testHelpers";
import { User } from "@/models/user";
import { prototype } from "vue/types/umd";

jest.mock("@/services/users");

describe("UserEd", () => {
    const user: User = { id: "0", fullname: "myName", login: "login", email: "test@test" };

    beforeEach(() => {
        (UsersService as jest.Mock<UsersService>).mockClear();
    });

    it("creates new user", async () => {
        const wrapper = factory(UserEdit)({ id: "add" });
        await wrapper.vm.$nextTick();

        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
        await wrapper.vm.$nextTick();

        expect(UsersService.prototype.getUser).not.toHaveBeenCalled();
    });

    it("displays user info", async () => {
        UsersService.prototype.getUser = jest.fn().mockImplementation(() => {
            return user;
        });
        const wrapper = factory(UserEdit)({ id: "123" });
        await wrapper.vm.$nextTick();

        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
        await wrapper.vm.$nextTick();

        expect(UsersService.prototype.getUser).toHaveBeenCalledWith("123");
        expect((wrapper.find("#fullname-1").element as HTMLInputElement).value).toBe("myName");
        expect((wrapper.find("#login-1").element as HTMLInputElement).value).toBe("login");
        expect((wrapper.find("#email-1").element as HTMLInputElement).value).toBe("test@test");
    });

    it("detects error getting user", async () => {
        UsersService.prototype.getUser = jest.fn().mockImplementation(() => {
            throw "cannot get user";
        });
        const wrapper = factory(UserEdit)({ id: "123" });
        await wrapper.vm.$nextTick();

        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
        expect(UsersService.prototype.getUser).toHaveBeenCalled();

        expect(wrapper.text()).toContain("sorry");
    });

    it("changes user info", async () => {
        const wrapper = factory(UserEdit)();

        // @ts-ignore: vm is our instance of UserEdit, but I cannot make ts see it.
        await wrapper.vm.confirmUser();
        expect(UsersService.prototype.saveUser).toHaveBeenCalled();
    });

    it("detect error when changing user info", async () => {
        UsersService.prototype.saveUser = jest.fn().mockImplementation(() => {
            throw "error";
        });
        const wrapper = factory(UserEdit)();

        // @ts-ignore: vm is our instance of UserEdit, but I cannot make ts see it.
        await wrapper.vm.confirmUser();

        expect(wrapper.text()).toContain("sorry");
    });

    it("deletes user", async () => {
        const wrapper = factory(UserEdit)();

        // @ts-ignore: vm is our instance of UserEdit, but I cannot make ts see it.
        await wrapper.vm.deleteUser();
        expect(UsersService.prototype.deleteUser).toHaveBeenCalled();
    });

    it("detect error when deleting user", async () => {
        UsersService.prototype.deleteUser = jest.fn().mockImplementation(() => {
            throw "error";
        });
        const wrapper = factory(UserEdit)();

        // @ts-ignore: vm is our instance of UserEdit, but I cannot make ts see it.
        await wrapper.vm.deleteUser();

        expect(wrapper.text()).toContain("sorry");
    });
});
