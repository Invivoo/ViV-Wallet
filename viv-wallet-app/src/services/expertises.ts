import { AxiosInstance } from "axios";
import { Expertise } from "@/models/expertise";
import { ServiceBase } from "./serviceBase";

export class ExpertisesService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http, `/expertises`);
    }

    async getExpertises(): Promise<Expertise[]> {
        return (await this.http.get<Expertise[]>("")).data;
    }
}
