import { AxiosInstance } from "axios";
import { User } from "../models/user";
import { ServiceBase } from "./serviceBase";

export class UsersService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http);
    }

    async getUsers(): Promise<User[]> {
        return (await this.http.get<User[]>("")).data;
    }

    async saveUser(user: User): Promise<Object> {
        if (!user.id) {
            return (await this.http.post<User>("", user)).data;
        }
        return (await this.http.put<User>(`${user.id}`, user)).data;
    }

    async getUser(id: string): Promise<User> {
        return (await this.http.get<User>(`${id}`)).data;
    }

    async deleteUser(user: User): Promise<any> {
        return (await this.http.delete<User>(`${user.id}`)).data;
    }
}
