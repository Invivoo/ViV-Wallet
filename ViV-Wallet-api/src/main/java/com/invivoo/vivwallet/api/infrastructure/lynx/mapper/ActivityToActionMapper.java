package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityToActionMapper {

    private final UserService userService;

    public ActivityToActionMapper(UserService userService) {
        this.userService = userService;
    }

    public Optional<Action> convert(Activity activity) {
        User user = userService.findByFullName(activity.getOwner())
                               .orElseGet(() -> userService.save(User.builder()
                                                                     .fullName(activity.getOwner())
                                                                     .build()));
        Action.ActionBuilder builder = Action.builder()
                                             .date(activity.getDate()) // todo actDate == valueDate ?
                                             .lynxActivityId(activity.getId())
                                             .achiever(user);
        setActivityTypeContext(activity, builder);
        return Optional.of(builder.build());
    }

    private void setActivityTypeContext(Activity activity, Action.ActionBuilder builder) {
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
                builder.type(ActionType.COOPTATION);
                builder.context(String.format("Cooptation de %s", activity.getRelatedTo()));
                break;
//            todo how to differentiate CORPORATE_PARTNERSHIP and SCHOOL_PARTNERSHIP
//            case INITIALISATION_DUN_PARTENARIAT:
//                builder.type(ActionType.CORPORATE_PARTNERSHIP);
//                builder.context(String.format("Partenariat Entreprise"));
//                builder.type(ActionType.SCHOOL_PARTNERSHIP);
//                builder.context(String.format("Partenariat École"));
//                break;

//            todo how to differentiate one hour and two hour formation and to get the formation name
//            case CREATION_DE_SUPPORT_DE_FORMATION:
//                builder.type(ActionType.ONE_HOUR_FORMATION_TRAINING_SUPPORT);
//                builder.context(String.format("Formation : %s", activity.getComment()));
//                builder.type(ActionType.TWO_HOURS_FORMATION_TRAINING_SUPPORT);
//                builder.context(String.format("Formation : %s", activity.getComment()));
//                break;
//            todo how to differentiate one hour and two hour formation and to get the formation name
//            case ANIMATION_DUNE_SESSION_DE_FORMATION_EN_PRESENTIEL:
//                builder.type(ActionType.ONE_HOUR_FORMATION_ANIMATION);
//                builder.context(String.format("Formation : %s", activity.getComment()));
//                builder.type(ActionType.TWO_HOURS_FORMATION_ALONE_ANIMATION);
//                builder.context(String.format("Formation : %s", activity.getComment()));
//                builder.type(ActionType.TWO_HOURS_FORMATION_MULTIPLE_ANIMATION);
//                builder.context(String.format("Formation : %s", activity.getComment()));
//                break;
//            case ANIMATION_DUNE_SESSION_DE_FORMATION_AU_FORMAT_WEBINAR:
//                builder.type(ActionType.ONE_HOUR_FORMATION_ANIMATION);
//                builder.context(String.format("Webinar : %s", activity.getComment()));
//                builder.type(ActionType.TWO_HOURS_FORMATION_ALONE_ANIMATION);
//                builder.context(String.format("Webinar : %s", activity.getComment()));
//                builder.type(ActionType.TWO_HOURS_FORMATION_MULTIPLE_ANIMATION);
//                builder.context(String.format("Webinar : %s", activity.getComment()));
//                break;
//            todo how to get the event name
//            case PARTICIPATION_A_UNE_CONFERENCE_EN_TANT_QUE_SPEAKER:
//                builder.type(ActionType.SPEAKER);
//                builder.context(String.format("Conférence : %s", activity.getComment()));
//            todo how to differentiate blog and other medium publication and get the publication name ?
//            case PUBLIER_UN_ARTICLE_COURT:
//                builder.type(ActionType.SHORT_ARTICLE_PUBLICATION);
//                builder.type(ActionType.SHORT_NEW_MEDIUM_FIRST_PUBLICATION);
//                builder.context(String.format("Article : %s", activity.getComment()));
//                break;
            case PUBLIER_UN_ARTICLE_MOYEN:
                builder.type(ActionType.ARTICLE_PUBLICATION);
//                builder.type(ActionType.NEW_MEDIUM_FIRST_PUBLICATION);
                builder.context(String.format("Article : %s", activity.getComment()));
                break;
//            case PUBLIER_UN_ARTICLE_LONG:
//                builder.type(ActionType.LONG_ARTICLE_PUBLICATION);
//                builder.type(ActionType.LONG_NEW_MEDIUM_FIRST_PUBLICATION);
//                builder.context(String.format("Article : %s", activity.getComment()));
//                break;
//            case PUBLIER_UN_LIVRE_BLANC:
//                builder.type(ActionType.WHITE_BOOK);
//                builder.context(String.format("Livre blanc : %s", activity.getComment()));
//                break;
//            case PUBLIER_UN_CHEAT_SHEET:
//                builder.type(ActionType.CHEAT_SHEET);
//                builder.context(String.format("Cheat Sheet : %s", activity.getComment()));
//                break;
            default:
                builder.type(ActionType.NO_MAPPING_FOUND);
                builder.context(String.format("No mapping found for %s", activity.getType()));
                break;
        }
    }
}
