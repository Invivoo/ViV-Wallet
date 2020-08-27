import { AxiosInstance } from "axios";
import { Consultant, ConsultantStatus } from "../models/consultant";
import { ServiceBase } from "./serviceBase";

export class ConsultantsService extends ServiceBase {
    constructor(expertiseName: string, http?: AxiosInstance) {
        super(http, `/expertises/${expertiseName}/members`);
    }

    async getConsultants(): Promise<Consultant[]> {
        const rawData = (await this.http.get("")).data;
        return rawData.map(consultant => {
            consultant.status = ConsultantStatus[consultant.status || ConsultantStatus.CONSULTANT_SENIOR];
            return consultant;
        });
    }

    async saveConsultant(consultant: Consultant): Promise<Object> {
        consultant.status = (ConsultantStatus[
            consultant.status || ConsultantStatus.CONSULTANT_SENIOR
        ] as unknown) as ConsultantStatus;
        if (!consultant.id) {
            return (await this.http.post<Consultant>("", consultant)).data;
        }
        return (await this.http.put<Consultant>(`${consultant.id}`, consultant)).data;
    }

    async getConsultant(id: string): Promise<Consultant> {
        const consultant = (await this.http.get(`${id}`)).data;
        consultant.status = ConsultantStatus[consultant.status];
        return consultant;
    }
}
