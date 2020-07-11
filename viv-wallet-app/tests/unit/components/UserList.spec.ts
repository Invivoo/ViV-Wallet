import UserList from "@/components/UserList.vue";
import { User } from "@/models/user";
import { factory } from "../testHelpers";

describe("UserList", () => {
    const user0: User = { id: "0", fullname: "myName0", login: "login0", email: "test0@test" };
    const user1: User = { id: "1", fullname: "myName1", login: "login1", email: "test1@test" };
    const users: User[] = [user0, user1];

    it("renders a list of user item", () => {
        const wrapper = factory(UserList)({ users });
        const userIdWrappers = wrapper.findAll("tbody>tr>td:first-child");
        expect(userIdWrappers.length).toEqual(2);
        expect(userIdWrappers.at(0).text()).toEqual(user0.id);
        expect(userIdWrappers.at(1).text()).toEqual(user1.id);
    });

    it("redirects to user selected edition page", async () => {
        const wrapper = factory(UserList)({ users });

        // @ts-ignore: vm is our instance of UserList, but I cannot make ts see it.
        wrapper.vm.onRowSelected(user0);

        expect(wrapper.vm.$router.currentRoute.path).toBe("/users/0");
    });
});
