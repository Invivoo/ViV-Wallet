import UserList from "@/components/UserList.vue";
import Users from "@/views/Users.vue";
import { User } from "@/models/user";
import { UsersService } from "@/services/users";
import { factory } from "../testHelpers";

jest.mock("@/services/users");

describe("Users", () => {
    beforeEach(() => {
        (UsersService as jest.Mock<UsersService>).mockClear();
    });

    it("needs the users service", () => {
        factory(Users)();
        expect(UsersService as jest.Mock<UsersService>).toHaveBeenCalled();
    });

    it("should render a UserList when the users service returns a list of users", async () => {
        const user: User = { id: "0", fullname: "myName", login: "login", email: "test@test" };

        UsersService.prototype.getUsers = jest.fn().mockImplementation(() => {
            return [user];
        });

        const wrapper = factory(Users)();
        await wrapper.vm.$nextTick();

        expect(UsersService.prototype.getUsers).toHaveBeenCalled();

        const userListWrapper = wrapper.find(UserList);
        expect(userListWrapper.element).toBeDefined();
        expect(userListWrapper.props().users).toEqual([user]);
    });

    it("should render an error message when an error occurs in the backend call", async () => {
        UsersService.prototype.getUsers = jest.fn().mockImplementation(() => {
            throw "cannot get users";
        });

        const errorMessage =
            "We're sorry, we're not able to retrieve this information at the moment, please try back later";

        const wrapper = factory(Users)();
        await wrapper.vm.$nextTick();

        expect(UsersService.prototype.getUsers).toHaveBeenCalled();

        const userListWrapper = wrapper.find(UserList);
        expect(userListWrapper.element).toBeUndefined();
        expect(wrapper.text()).toEqual(errorMessage);
    });

    it("should render a loading message when waiting for backend response", () => {
        const loadingMessage = "Loading...";
        const wrapper = factory(Users)();

        const userListWrapper = wrapper.find(UserList);
        expect(userListWrapper.element).toBeUndefined();
        expect(wrapper.text()).toEqual(loadingMessage);
    });
});
