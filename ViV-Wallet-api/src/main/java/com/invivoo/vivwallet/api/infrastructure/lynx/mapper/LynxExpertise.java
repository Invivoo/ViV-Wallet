package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;
@AllArgsConstructor
@Getter
public enum LynxExpertise {
    C_PLUS_PLUS("C++", Expertise.PROGRAMMATION_C_PLUS_PLUS),
    PYTHON("Python", Expertise.PROGRAMMATION_PYTHON),
    FRONT_END("Front-End", Expertise.FRONT_END),
    C_SHARP("C#", Expertise.PROGRAMMATION_C_SHARP),
    JAVA("Java", Expertise.PROGRAMMATION_JAVA),
    DEVOPS("Devops", Expertise.DEVOPS),
    SUPPORT_ET_PROD_APPLICATIVE("Support & Prod Applicative", Expertise.SUPPORT_ET_PRODUCTION_APPLICATIVE),

    BIG_DATE_ENGINEERING("Big Data Engineering", Expertise.BIG_DATA),
    DATA_SCIENCE("Data Science", Expertise.MACHINE_LEARNING),
    ANALYTICS_ET_BI("Analytics & BI", Expertise.ANALYTICS_ET_BI),

    RISK("Risques", Expertise.RISQUES),
    RISK_ET_REGULATORY_ANALYTICS("Risk & Regulatory Analytics", Expertise.RISQUES),
    POST_TRADE("Post Trade", Expertise.POST_TRADE),
    FRONT_OFFICE("Front-Office", Expertise.MODELISATION_ET_PRICING);

    private final String lynxExpertiseName;
    private final Expertise expertiseValue;

    public static Optional<LynxExpertise> forName(String name) {
        return Stream.of(LynxExpertise.values())
                     .filter(e -> e.lynxExpertiseName.equals(name))
                     .findFirst();
    }

}
