import { AxiosInstance } from "axios";
import { Consultant, ConsultantStatus } from "../models/consultant";
import { ServiceBase } from "./serviceBase";

export class ConsultantsService extends ServiceBase {
    constructor(expertiseName: string, http?: AxiosInstance) {
        super(http, `/expertises/${expertiseName}/members`);
    }

    async getConsultants(): Promise<Consultant[]> {
        return (await this.http.get("")).data.map(normalizeRawConsultant);
    }

    async saveConsultant(consultant: Consultant): Promise<Object> {
        return (await this.http.put<Consultant>(`${consultant.id}`, consultantToRaw(consultant))).data;
    }

    async getConsultant(id: string): Promise<Consultant> {
        return normalizeRawConsultant((await this.http.get(`${id}`)).data);
    }
}

export function normalizeRawConsultant(consultant: any): Consultant {
    consultant.status = ConsultantStatus[consultant.status || ConsultantStatus.CONSULTANT_SENIOR];
    return consultant;
}

export function consultantToRaw(consultant: Consultant): any {
    consultant.status = (ConsultantStatus[
        consultant.status || ConsultantStatus.CONSULTANT_SENIOR
    ] as unknown) as ConsultantStatus;
    return consultant;
}
