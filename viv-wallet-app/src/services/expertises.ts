import { AxiosInstance } from "axios";
import { ServiceBase } from "./serviceBase";
import { Expertise } from "@/models/expertise";

export class ExpertisesService extends ServiceBase {
    constructor(http?: AxiosInstance) {
        super(http, `/expertises`);
    }

    async getExpertises(): Promise<Expertise[]> {
        return (await this.http.get("")).data;
    }
}
