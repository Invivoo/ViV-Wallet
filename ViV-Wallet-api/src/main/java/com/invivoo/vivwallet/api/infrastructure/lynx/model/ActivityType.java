package com.invivoo.vivwallet.api.infrastructure.lynx.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ActivityType {
    COACHING_HORS_OPPORTUNITE("Coach TechFcl hors Opp"),
    COACHING_SUR_OPPORTUNITE("Coach TechFcl sur Opp "),
    COACHING_SUR_OPPORTUNITE_GAGNEE("Coach TechFcl sur Opp gagnée"),

    PRESCRIPTEUR_SUR_AO("Relaying Opp"),
    REMONTEE_AO("Influencing Opp"),

    EVALUATION_TECHNIQUE_OK_SUR_PROFIL("Eval TechFcl-OK-Profil"),
    EVALUATION_TECHNIQUE_OK_SUR_PROJET("Eval TechFcl-OK-Projet"),
    EVALUATION_TECHNIQUE_NOK("Eval TechFcl-NOK"),

    COOPTATION("Cooptation"),

    INITIALISATION_DUN_PARTENARIAT("Partnership:Accord"),

    CREATION_DE_SUPPORT_DE_FORMATION_2H_HTT("Training:Supports2H-HTT"),
    CREATION_DE_SUPPORT_DE_FORMATION_1J("Training:Supports1J"),
    CREATION_DE_SUPPORT_DE_FORMATION_1J_HTT("Training:Supports1J-HTT"),
    ANIMATION_SESSION_DE_FORMATION_2H_HTT("Training:Session2H-HTT"),
    ANIMATION_SESSION_DE_FORMATION_1J("Training:Session1J"),
    ANIMATION_SESSION_DE_FORMATION_WEBINAR("Training:Webinar"),
    PARTICIPATION_A_UNE_CONFERENCE_EN_TANT_QUE_SPEAKER("Event:Talk"),
    RETEX("Event:RETEX"),

    PUBLIER_UN_ARTICLE_COURT("Publish:Article court"),
    PUBLIER_UN_ARTICLE_MOYEN("Publish:Article moyen"),
    PUBLIER_UN_ARTICLE_LONG("Publish:Article long"),
    PUBLIER_UN_LIVRE_BLANC("Publish:Livre blanc"),
    PUBLIER_UN_CHEAT_SHEET("Publish:Cheat Sheet"),
    INTERVIEW("Publish:Interview"),

    AUDIT_CIR_PHASE_1("Audit R&D - phase 1"),
    AUDIT_CIR_PHASE_2("Audit R&D - phase 2"),

    DEV_SMALL("Dev:Small"),
    DEV_MEDIUM("Dev:Medium"),
    DEV_LARGE("Dev:Large"),
    DEV_EXTRA_LARGE("Dev:XLarge");

    private final String name;

    @JsonCreator
    public static ActivityType forValue(String value) {
        return Stream.of(ActivityType.values())
                     .filter(a -> a.getName().equals(value))
                     .findFirst()
                     .orElse(null);
    }

    @JsonValue
    public String toValue(ActivityType activityType) {
        return activityType.getName();
    }
}
