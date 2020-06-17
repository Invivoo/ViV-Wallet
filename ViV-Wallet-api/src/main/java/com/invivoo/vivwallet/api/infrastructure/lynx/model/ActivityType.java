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
    COACHING_SUR_OPPORTUNITE("Coach TechFcl sur Opp"),
    COACHING_SUR_OPPORTUNITE_GAGNEE("Coach TechFcl sur Opp gagnée"),

    PRESCRIPTEUR_SUR_AO("Relaying Opp"),
    REMONTEE_AO("Influencing Opp"),

    EVALUATION_TECHNIQUE_OK_SUR_PROFIL("Eval TechFcl-OK-Profil"),
    EVALUATION_TECHNIQUE_OK_SUR_PROJET("Eval TechFcl-OK-Projet"),
    EVALUATION_TECHNIQUE_NOK("Eval TechFcl-NOK"),

    COOPTATION("Cooptation"),

    INITIALISATION_DUN_PARTENARIAT("Partnership:Accord"),

    CREATION_DE_SUPPORT_DE_FORMATION("Training:Supports"),
    ANIMATION_DUNE_SESSION_DE_FORMATION_EN_PRESENTIEL("Training:Session"),
    ANIMATION_DUNE_SESSION_DE_FORMATION_AU_FORMAT_WEBINAR("Training:Webinar"),
    PARTICIPATION_A_UNE_CONFERENCE_EN_TANT_QUE_SPEAKER("Event:Talk"),

    PUBLIER_UN_ARTICLE_COURT("Publish:Article court"),
    PUBLIER_UN_ARTICLE_MOYEN("Publish:Article moyen"),
    PUBLIER_UN_ARTICLE_LONG("Publish:Article long"),
    PUBLIER_UN_LIVRE_BLANC("Publish:Livre blanc"),
    PUBLIER_UN_CHEAT_SHEET("Publish:Cheat Sheet");

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
