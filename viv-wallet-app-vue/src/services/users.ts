import axios from "axios";
import { User } from '../models/user';
import { BACKEND_BASE_URL, REQUEST_TIMEOUT_MS } from "../config/constants";

export class UsersService {

    async getUsers(): Promise<User[]> {
        return (await axios.get<User[]>(`${BACKEND_BASE_URL}/users`, { timeout: REQUEST_TIMEOUT_MS })).data;
    }

    async saveUser(user: User): Promise<Object> {
        if (!user.id) {
            return (await axios.post<User>(`${BACKEND_BASE_URL}/users`, user, { timeout: REQUEST_TIMEOUT_MS })).data;
        }
        return (await axios.put<User>(`${BACKEND_BASE_URL}/users/${user.id}`, user, { timeout: REQUEST_TIMEOUT_MS })).data;
    }

    async getUser(id: string): Promise<User> {
        return (await axios.get<User>(`${BACKEND_BASE_URL}/users/${id}`, { timeout: REQUEST_TIMEOUT_MS })).data;
    }

    async deleteUser(user: User): Promise<any> {
        return (await axios.delete<User>(`${BACKEND_BASE_URL}/users/${user.id}`, { timeout: REQUEST_TIMEOUT_MS })).data;
    }
}
