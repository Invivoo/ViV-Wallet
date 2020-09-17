package com.invivoo.vivwallet.api.domain.expertise;

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

    private Expertise(String expertiseName) {
        this.expertiseName = expertiseName;
    }

    public String getExpertiseName() {
        return this.expertiseName;
    }
}
