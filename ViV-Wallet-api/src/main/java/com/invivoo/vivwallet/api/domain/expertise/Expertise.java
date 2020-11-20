package com.invivoo.vivwallet.api.domain.expertise;

import java.util.Optional;
import java.util.stream.Stream;

public enum Expertise {
    PROGRAMMATION_C_PLUS_PLUS("Programmation C++"),
    PROGRAMMATION_PYTHON("Programmation Python"),
    FRONT_END("Front-End"),
    MACHINE_LEARNING("Machine Learning"),
    MODELISATION_ET_PRICING("Modélisation & Pricing"),
    PROGRAMMATION_C_SHARP("Programmation C#"),
    PROGRAMMATION_JAVA("Programmation Java"),
    RISQUES("Risques"),
    BIG_DATA("Big Data"),
    POST_TRADE("Post-Trade"),
    AGILITE_ET_CRAFT("Agilité & Craft"),
    DEVOPS("Devops");

    private String expertiseName;

    Expertise(String expertiseName) {
        this.expertiseName = expertiseName;
    }

    public String getExpertiseName() {
        return this.expertiseName;
    }

    public static Optional<Expertise> forName(String name) {
        return Stream.of(Expertise.values())
                     .filter(e -> e.name().equals(name))
                     .findFirst();
    }
}
