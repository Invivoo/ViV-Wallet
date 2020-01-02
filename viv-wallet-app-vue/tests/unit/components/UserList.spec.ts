import UserList from "@/components/UserList.vue";
import { User } from "@/models/user";
import { factory } from "../testHelpers";
import { BTable } from "bootstrap-vue";

describe("UserList", () => {
    it("renders a list of user item", () => {
        const user0: User = { id: "0", fullname: "myName0", login: "login0", email: "test0@test" };
        const user1: User = { id: "1", fullname: "myName1", login: "login1", email: "test1@test" };
        const users: User[] = [user0, user1];
        const wrapper = factory(UserList)({ users });
        const userItems = wrapper.find(BTable).props().items;
        expect(userItems.length).toEqual(2);
        expect(userItems).toContainEqual(user0);
        expect(userItems).toContainEqual(user1);
    });
});
