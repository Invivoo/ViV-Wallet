import { AxiosInstance } from "axios";
import { Consultant } from "../models/consultant";
import { ServiceBase } from "./serviceBase";

export class ConsultantsService extends ServiceBase {
    constructor(expertiseName: string, http?: AxiosInstance) {
        super(http, `/expertises/${expertiseName}/members`);
    }

    async getConsultants(): Promise<Consultant[]> {
        return (await this.http.get<Consultant[]>("")).data;
    }

    async saveConsultant(consultant: Consultant): Promise<Object> {
        if (!consultant.id) {
            return (await this.http.post<Consultant>("", consultant)).data;
        }
        return (await this.http.put<Consultant>(`${consultant.id}`, consultant)).data;
    }

    async getConsultant(id: string): Promise<Consultant> {
        return (await this.http.get<Consultant>(`${id}`)).data;
    }

}
