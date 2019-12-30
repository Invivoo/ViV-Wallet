import { shallowMount } from "@vue/test-utils";
import UserItem from "@/components/UserItem.vue";
import { User } from "@/models/user";
import { factory } from "../testHelpers";

describe("UserItem", () => {
    it("renders the user login", () => {
        const user: User = { id: "0", fullname: "myName", login: "login", email: "test@test" };
        const wrapper = factory(UserItem)({ user });
        expect(wrapper.text()).toEqual(user.login);
    });
});
