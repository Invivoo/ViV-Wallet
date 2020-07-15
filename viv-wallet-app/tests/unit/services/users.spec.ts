import { User } from "@/models/user";
import axios from "axios";
import { UsersService } from "@/services/users";

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("UsersService", () => {
    let service: UsersService;

    beforeEach(() => {
        mockedAxios.get.mockClear();
        mockedAxios.get.mockReturnValue(Promise.resolve({}));

        service = new UsersService(mockedAxios);
    });

    const prototypeUser: User = {
        id: "123",
        login: "mylogin",
        fullname: "My LOGIN",
        email: "my.login@invivoo.com"
    };

    it("should be created", () => {
        expect(service).toBeTruthy();
    });

    it("should get all users", async () => {
        const response = {
            data: [prototypeUser]
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        expect(await service.getUsers()).toEqual(response.data);

        expect(mockedAxios.get).toHaveBeenCalledWith("");
    });

    it("should get user by id", async () => {
        const response = {
            data: { ...prototypeUser, id: "id1", login: "lid1" }
        };
        mockedAxios.get.mockReturnValue(Promise.resolve(response));

        const returnedUser = await service.getUser("id1");
        expect(returnedUser.id).toEqual("id1");
        expect(returnedUser.login).toEqual("lid1");

        expect(mockedAxios.get).toHaveBeenCalledWith(`id1`);
    });

    it("should delete user by id", async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({}));

        await service.deleteUser(prototypeUser);

        expect(mockedAxios.delete).toHaveBeenCalledWith(`123`);
    });

    it("should post user if there's no id", async () => {
        const response = {
            data: prototypeUser
        };
        mockedAxios.post.mockReturnValue(Promise.resolve(response));

        const postedUser = { ...prototypeUser, id: undefined };
        expect(await service.saveUser(postedUser)).toEqual(prototypeUser);

        expect(mockedAxios.post).toHaveBeenCalledWith("", postedUser);
    });

    it("should update user if there's an id", async () => {
        const response = {
            data: prototypeUser
        };
        mockedAxios.put.mockReturnValue(Promise.resolve(response));

        expect(await service.saveUser(prototypeUser)).toEqual(prototypeUser);

        expect(mockedAxios.put).toHaveBeenCalledWith(`123`, prototypeUser);
    });
});
