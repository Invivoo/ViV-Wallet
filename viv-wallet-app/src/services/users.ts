import axios, { AxiosInstance } from "axios";
import { User } from "../models/user";
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "../config/constants";
import { getToken } from "x4b-ui";

export class UsersService {

    private http: AxiosInstance;

    constructor(http?: AxiosInstance) {
        this.http = http || axios.create({
            baseURL: `${BACKEND_BASE_URL}/users`,
            timeout: REQUEST_TIMEOUT_MS,
            headers: { 'Authorization': `Bearer ${getToken()}` },
            withCredentials: true
        });
    }

    async getUsers(): Promise<User[]> {
        return (await this.http.get<User[]>('')).data;
    }

    async saveUser(user: User): Promise<Object> {
        if (!user.id) {
            return (await this.http.post<User>('', user)).data;
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
