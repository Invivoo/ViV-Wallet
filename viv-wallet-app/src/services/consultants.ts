import { AxiosInstance } from "axios";
import { Consultant, ConsultantStatus } from "../models/consultant";
import { ServiceBase } from "./serviceBase";

export class ConsultantsService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http);
    }

    async getConsultants(expertiseName?: string): Promise<Consultant[]> {
        if (expertiseName) {
            return (await this.http.get(`/users?expertise=${expertiseName}`)).data.map(normalizeRawConsultant);
        } else {
            return (await this.http.get(`/users`)).data.map(normalizeRawConsultant);
        }
    }

    async saveExpertise(consultant: Consultant): Promise<Object> {
        return (await this.http.post<any>(`/users/${consultant.id}/expertises`, consultantToRaw(consultant))).data;
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
