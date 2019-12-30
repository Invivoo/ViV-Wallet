import { shallowMount } from "@vue/test-utils";
import UserList from "@/components/UserList.vue";
import Users from "@/views/Users.vue";
import { User } from "@/models/user";
import axios from "axios";
import { BACKEND_BASE_URL } from "@/config/constants";
import { factory } from "../testHelpers";

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Users", () => {
    beforeEach(() => {
        mockedAxios.get.mockClear();
        mockedAxios.get.mockReturnValue(Promise.resolve({}));
    });

    it("should render a UserList when the backend returns a list of users", async () => {
        const user: User = { id: "0", fullname: "myName", login: "login", email: "test@test" };

        const response = {
            data: [user]
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const wrapper = factory(Users)();
        await wrapper.vm.$nextTick();

        expect(mockedAxios.get).toHaveBeenCalledWith(`${BACKEND_BASE_URL}/users`, { timeout: 10000 });
        const userListWrapper = wrapper.find(UserList);
        expect(userListWrapper.element).toBeDefined();
        expect(userListWrapper.props().users).toEqual(response.data);
    });

    it("should render an error message when an error occurs in the backend call", async () => {
        mockedAxios.get.mockReturnValue(Promise.reject("cannot get users"));
        const errorMessage =
            "We're sorry, we're not able to retrieve this information at the moment, please try back later";

        const wrapper = factory(Users)();
        await wrapper.vm.$nextTick();

        expect(mockedAxios.get).toHaveBeenCalledWith(`${BACKEND_BASE_URL}/users`, { timeout: 10000 });
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
