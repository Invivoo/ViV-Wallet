package com.invivoo.vivwallet.api.domain.expertise;

public enum Expertise {
    PROGRAMMATION_JAVA("Programmation Java"),
    PROGRAMMATION_PYTHON("Programmation Python"),
    PROGRAMMATION_CSHARP("Programmation C#");

    private String expertiseName;

    Expertise(String expertiseName) {
        this.expertiseName = expertiseName;
    }

    public String getExpertiseName() {
        return this.expertiseName;
    }
}
