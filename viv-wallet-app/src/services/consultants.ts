import { Consultant, ConsultantStatus } from "../models/consultant";
import { ServiceBase } from "./serviceBase";

type RawConsultant = Omit<Consultant, "status"> & {
    status: keyof typeof ConsultantStatus;
};

export class ConsultantsService extends ServiceBase {
    async getConsultants(expertiseName?: string): Promise<Consultant[]> {
        return expertiseName
            ? (await this.http.get<RawConsultant[]>(`/users?expertise=${expertiseName}`)).data.map((rawConsultant) =>
                  normalizeRawConsultant(rawConsultant)
              )
            : (await this.http.get<RawConsultant[]>(`/users`)).data.map((rawConsultant) =>
                  normalizeRawConsultant(rawConsultant)
              );
    }

    async saveExpertise(consultant: Consultant): Promise<Object> {
        return (await this.http.post<RawConsultant>(`/users/${consultant.id}/expertises`, consultantToRaw(consultant)))
            .data;
    }

    async getConsultant(id: string): Promise<Consultant> {
        return normalizeRawConsultant((await this.http.get(`/users/${id}`)).data);
    }
}

export function normalizeRawConsultant(rawConsultant: RawConsultant): Consultant {
    return {
        ...rawConsultant,
        status: ConsultantStatus[rawConsultant.status || ConsultantStatus.CONSULTANT],
    };
}

export function consultantToRaw(consultant: Consultant): RawConsultant {
    return {
        ...consultant,
        status: ConsultantStatus[consultant.status || ConsultantStatus.CONSULTANT],
    };
}
