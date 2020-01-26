import { User } from "@/models/user";
import { BACKEND_BASE_URL } from "@/config/constants";
import axios from "axios";
import { UsersService } from "@/services/users";

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("UsersService", () => {
    let service: UsersService;

    beforeEach(() => {
        mockedAxios.get.mockClear();
        mockedAxios.get.mockReturnValue(Promise.resolve({}));

        service = new UsersService();
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

    it("should post user if there's no id", async () => {
        const response = {
            data: prototypeUser
        };
        mockedAxios.post.mockReturnValue(Promise.resolve(response));

        const postedUser = { ...prototypeUser, id: undefined };
        expect(await service.saveUser(postedUser)).toEqual(prototypeUser);

        expect(mockedAxios.post).toHaveBeenCalledWith(`${BACKEND_BASE_URL}/users`, postedUser, { timeout: 10000 });
    });

    it("should update user if there's an id", async () => {
        const response = {
            data: prototypeUser
        };
        mockedAxios.put.mockReturnValue(Promise.resolve(response));

        expect(await service.saveUser(prototypeUser)).toEqual(prototypeUser);

        expect(mockedAxios.put).toHaveBeenCalledWith(`${BACKEND_BASE_URL}/users/123`, prototypeUser, {
            timeout: 10000
        });
    });
});
