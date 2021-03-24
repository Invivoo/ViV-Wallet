package com.invivoo.vivwallet.api.domain.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionType {
    COACHING("Coaching", 10, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS),
    SUCCESSFUL_COACHING("Coaching gagnant", 40, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    TECHNICAL_ASSESSMENT("Évaluation technique", 10, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS),

    INFLUENCING_OPPORTUNITY("Impact sur la décision client", 100, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    RELAYING_OPPORTUNITY("Remontée d’AO gagnante", 100, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),

    COOPTATION("Cooptation", 400, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    COOPTATION_NOK("Cooptation NOK", 0, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS),

    SCHOOL_PARTNERSHIP("Initier un partenariat école", 25, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    CORPORATE_PARTNERSHIP("Initier un partenariat entreprise", 100, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),

    ONE_HOUR_FORMATION_TRAINING_SUPPORT("Formation interne 1 session d’1h – Création des supports", 25, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    ONE_HOUR_FORMATION_ANIMATION("Formation interne 1 session d’1h – Animation", 15, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    TWO_HOURS_FORMATION_TRAINING_SUPPORT("Formation interne de 2h sur 1 à 3 sessions max – Création des supports", 50, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    TWO_HOURS_FORMATION_ANIMATION("Formation interne de 2h sur 1 à 3 sessions max – Animation", 30, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    SPEAKER("Speaker conference", 100, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),

    SHORT_ARTICLE_PUBLICATION("Article court 100 – 500 mots", 5, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    ARTICLE_PUBLICATION("Article moyen 500 – 2500 mots", 15, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    LONG_ARTICLE_PUBLICATION("Article long >2500 mots", 20, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    WHITE_BOOK("Livre blanc", 100, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    CHEAT_SHEET("Cheat Sheet", 10, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),

    AUDIT_CIR_PHASE_1("Audit CIR - phase 1", 20, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS),
    AUDIT_CIR_PHASE_2("Audit CIR - phase 2", 30, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS),

    PROJET_FIL_ROUGE_TICKET_S("Projet Fil Rouge - ticket S", 10, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    PROJET_FIL_ROUGE_TICKET_M("Projet Fil Rouge - ticket M", 15, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    PROJET_FIL_ROUGE_TICKET_L("Projet Fil Rouge - ticket L", 25, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),
    PROJET_FIL_ROUGE_TICKET_XL("Projet Fil Rouge - ticket XL", 50, Constants.IS_SHARED_BY_MULTIPLE_ACHIEVERS),

    NO_MAPPING_FOUND("No mapping found", 0, Constants.IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS);

    private final String name;
    private final Integer value;
    private final boolean isSharedByAchievers;

    private static class Constants {
        public static final boolean IS_NOT_SHARED_BY_MULTIPLE_ACHIEVERS = false;
        public static final boolean IS_SHARED_BY_MULTIPLE_ACHIEVERS = true;
    }
}
