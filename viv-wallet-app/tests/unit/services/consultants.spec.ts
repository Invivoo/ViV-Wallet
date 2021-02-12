import { Consultant, ConsultantStatus } from "@/models/consultant";
import { normalizeRawConsultant, consultantToRaw } from "@/services/consultants";

describe("ConsultantsService helper functions", () => {
    it("should normalize before saving", () => {
        expect(
            consultantToRaw({
                startDate: "2020-09-12",
                status: ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING,
                id: "4",
                user: "rflondingjava",
                fullName: "Roger Flodin Java",
                email: "roget.flondin.java@invivoo.com",
            } as Consultant).status
        ).toBe("CONSULTANT_SENIOR_IN_ONBOARDING");
    });

    it("should read raw", () => {
        expect(
            normalizeRawConsultant({
                startDate: "2020-09-12",
                status: "CONSULTANT_SENIOR_IN_ONBOARDING",
                id: "4",
                user: "rflondingjava",
                fullName: "Roger Flodin Java",
                email: "roget.flondin.java@invivoo.com",
            }).status
        ).toBe(ConsultantStatus.CONSULTANT_SENIOR_IN_ONBOARDING);
    });
});
