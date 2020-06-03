import UserEdit from "@/views/UserEdit.vue";
import { UsersService } from "@/services/users";
import { factory } from "../testHelpers";
import { User } from "@/models/user";

jest.mock("@/services/users");

describe("UserEd", () => {
    const user: User = { id: "0", fullname: "myName", login: "login", email: "test@test" };

    beforeEach(() => {
        (UsersService as jest.Mock<UsersService>).mockClear();
    });

    it("displays user info", async () => {
        UsersService.prototype.getUser = jest.fn().mockImplementation(() => {
            return user;
        });
        const wrapper = factory(UserEdit)();
        await wrapper.vm.$nextTick();

        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#fullname-1').props().value).toBe("myName");
        expect(wrapper.find('#login-1').props().value).toBe("login");
        expect(wrapper.find('#email-1').props().value).toBe("test@test");
    });

    it("detects error getting user", async () => {
        UsersService.prototype.getUser = jest.fn().mockImplementation(() => {
            throw "cannot get user";
        });
        const wrapper = factory(UserEdit)();
        await wrapper.vm.$nextTick();

        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
        expect(UsersService.prototype.getUser).toHaveBeenCalled();

        expect(wrapper.text()).toContain("sorry");
    });
});
