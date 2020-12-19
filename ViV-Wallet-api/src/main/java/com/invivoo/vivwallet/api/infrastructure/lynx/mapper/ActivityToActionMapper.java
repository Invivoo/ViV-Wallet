package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ActivityToActionMapper {

    private final UserService userService;

    public ActivityToActionMapper(UserService userService) {
        this.userService = userService;
    }

    public Optional<Action> convert(Activity activity) {
        String owner = getCleanedOwnerFullName(activity);
        User user = userService.findByFullName(owner)
                               .orElseGet(() -> userService.save(User.builder()
                                                                     .fullName(owner)
                                                                     .build()));
        Action.ActionBuilder builder = Action.builder()
                                             .date(activity.getDate()) // todo actDate == valueDate ?
                                             .lynxActivityId(activity.getId())
                                             .achiever(user);
        setActivityTypeContext(activity, builder);
        return Optional.of(builder.build());
    }

    private String getCleanedOwnerFullName(Activity activity) {
        String owner = activity.getOwner().trim();
        owner = owner.substring(0, owner.contains("@") ? owner.indexOf("@") : owner.length());
        owner = owner.contains("Marcos Aurelio ALMEIDA DA SILVA") ? "Marcos ALMEIDA" : owner;
        owner = owner.contains("Gustavo OLIVEIRA") ? "Gustavo OLIVEIRA" : owner;
        return owner;
    }

    private void setActivityTypeContext(Activity activity, Action.ActionBuilder builder) {
        long activityDuration = getActivityDuration(activity);
        switch (activity.getType()) {
            case COACHING_HORS_OPPORTUNITE:
            case COACHING_SUR_OPPORTUNITE:
                builder.type(ActionType.COACHING);
                builder.context(String.format("Coaching de %s", activity.getRelatedTo()));
                break;
            case COACHING_SUR_OPPORTUNITE_GAGNEE:
                builder.type(ActionType.SUCCESSFUL_COACHING);
                builder.context(String.format("Coaching gagnant de %s", activity.getRelatedTo()));
                break;
            case PRESCRIPTEUR_SUR_AO:
                builder.type(ActionType.RELAYING_OPPORTUNITY);
                builder.context(String.format("Remontée d’AO gagnante : %s", activity.getComment()));
                break;
            case REMONTEE_AO:
                builder.type(ActionType.INFLUENCING_OPPORTUNITY);
                builder.context(String.format("Impact sur la décision client : %s", activity.getComment()));
                break;
            case EVALUATION_TECHNIQUE_OK_SUR_PROFIL:
                builder.type(ActionType.TECHNICAL_ASSESSMENT);
                builder.context(String.format("Évaluation de %s : candidat retenu sur profil !", activity.getRelatedTo()));
                break;
            case EVALUATION_TECHNIQUE_OK_SUR_PROJET:
                builder.type(ActionType.TECHNICAL_ASSESSMENT);
                builder.context(String.format("Évaluation de %s : candidat retenu sur projet !", activity.getRelatedTo()));
                break;
            case EVALUATION_TECHNIQUE_NOK:
                builder.type(ActionType.TECHNICAL_ASSESSMENT);
                builder.context(String.format("Évaluation de %s : candidat non retenu", activity.getRelatedTo()));
                break;
            case COOPTATION:
                if ("OK".equalsIgnoreCase(activity.getValidity())) {
                    builder.type(ActionType.COOPTATION);
                } else {
                    builder.type(ActionType.COOPTATION_NOK);
                }
                builder.context(String.format("Cooptation de %s", activity.getRelatedTo()));
                break;
            case INITIALISATION_DUN_PARTENARIAT:
                if (activity.getOpportunityLabel().contains("Partnership/School")) {
                    builder.type(ActionType.SCHOOL_PARTNERSHIP);
                    builder.context(String.format("Partenariat École : %s %s", activity.getComment(), activity.getOpportunity()));
                } else {
                    builder.type(ActionType.CORPORATE_PARTNERSHIP);
                    builder.context(String.format("Partenariat Entreprise : %s %s", activity.getComment(), activity.getOpportunity()));
                }
                break;
            case CREATION_DE_SUPPORT_DE_FORMATION:
                if (activityDuration <= 1) {
                    builder.type(ActionType.ONE_HOUR_FORMATION_TRAINING_SUPPORT);
                } else {
                    builder.type(ActionType.TWO_HOURS_FORMATION_TRAINING_SUPPORT);
                }
                builder.context(String.format("Support Formation : %s", activity.getComment()));
                break;
            case ANIMATION_DUNE_SESSION_DE_FORMATION_EN_PRESENTIEL:
                if (activityDuration <= 1) {
                    builder.type(ActionType.ONE_HOUR_FORMATION_ANIMATION);
                } else {
                    builder.type(ActionType.TWO_HOURS_FORMATION_ANIMATION);
                }
                builder.context(String.format("Animation Formation : %s", activity.getComment()));
                break;
            case ANIMATION_DUNE_SESSION_DE_FORMATION_AU_FORMAT_WEBINAR:
                if (activityDuration <= 1) {
                    builder.type(ActionType.ONE_HOUR_FORMATION_ANIMATION);
                } else {
                    builder.type(ActionType.TWO_HOURS_FORMATION_ANIMATION);
                }
                builder.context(String.format("Animation Formation Webinar : %s", activity.getComment()));
                break;
            case PARTICIPATION_A_UNE_CONFERENCE_EN_TANT_QUE_SPEAKER:
                builder.type(ActionType.SPEAKER);
                builder.context(String.format("Conférence : %s %s", activity.getComment(), activity.getOpportunity()));
                break;
            case PUBLIER_UN_ARTICLE_COURT:
                builder.type(ActionType.SHORT_ARTICLE_PUBLICATION);
                builder.context(String.format("Article : %s", activity.getComment()));
                break;
            case PUBLIER_UN_ARTICLE_MOYEN:
                builder.type(ActionType.ARTICLE_PUBLICATION);
                builder.context(String.format("Article : %s", activity.getComment()));
                break;
            case PUBLIER_UN_ARTICLE_LONG:
                builder.type(ActionType.LONG_ARTICLE_PUBLICATION);
                builder.context(String.format("Article : %s", activity.getComment()));
                break;
            case PUBLIER_UN_LIVRE_BLANC:
                builder.type(ActionType.WHITE_BOOK);
                builder.context(String.format("Livre blanc : %s", activity.getComment()));
                break;
            case PUBLIER_UN_CHEAT_SHEET:
                builder.type(ActionType.CHEAT_SHEET);
                builder.context(String.format("Cheat Sheet : %s", activity.getComment()));
                break;
            default:
                builder.type(ActionType.NO_MAPPING_FOUND);
                builder.context(String.format("No mapping found for %s", activity.getType()));
                break;
        }
    }

    private long getActivityDuration(Activity activity) {
        return Optional.ofNullable(activity.getDate())
                       .orElse(LocalDateTime.MIN)
                       .until(Optional.ofNullable(activity.getEndDate()).orElse(LocalDateTime.MAX), ChronoUnit.HOURS);
    }
}
