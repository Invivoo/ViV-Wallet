import UserList from "@/components/UserList.vue";
import Loading from "@/components/Loading.vue";
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
        const user: User = { id: "0", fullname: "myName", user: "login", email: "test@test" };

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

        const wrapper = factory(Users)();
        await wrapper.vm.$nextTick();

        expect(UsersService.prototype.getUsers).toHaveBeenCalled();
        const loadingWrapper = wrapper.find(Loading);
        expect(loadingWrapper.props().errored).toBeTruthy();
        expect(loadingWrapper.props().loading).toBeFalsy();
    });

    it("should render a loading message when waiting for backend response", () => {
        const wrapper = factory(Users)();

        const loadingWrapper = wrapper.find(Loading);
        expect(loadingWrapper.props().errored).toBeFalsy();
        expect(loadingWrapper.props().loading).toBeTruthy();
    });
});
