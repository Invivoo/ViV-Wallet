package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionStatus;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ActivityToActionMapper {

    private final UserRepository userRepository;

    public ActivityToActionMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Action> convert(Activity activity) {
        String owner = getCleanedOwnerFullName(activity);
        Optional<User> achieverOpt = Optional.ofNullable(activity.getParticipantEmail())
                                             .filter(StringUtils::isNotBlank)
                                             .flatMap(userRepository::findByEmail)
                                             .or(() -> userRepository.findByFullNameTrimIgnoreCase(owner));
        if (achieverOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = achieverOpt.get();
        Action.ActionBuilder builder = Action.builder()
                                             .date(activity.getDate())
                                             .valueDate(activity.getValueDate())
                                             .lynxActivityId(activity.getId())
                                             .status(ActionStatus.TO_VALIDATE)
                                             .achiever(user);
        setActivityTypeContext(activity, user, builder);
        return Optional.of(builder.build());
    }

    private String getCleanedOwnerFullName(Activity activity) {
        String owner = activity.getOwner().trim();
        owner = owner.substring(0, owner.contains("@") ? owner.indexOf("@") : owner.length());
        owner = owner.contains("Marcos Aurelio ALMEIDA DA SILVA") ? "Marcos ALMEIDA" : owner;
        owner = owner.contains("Gustavo OLIVEIRA") ? "Gustavo OLIVEIRA" : owner;
        owner = owner.contains("Nhi TRAN") ? "Nhi TRAN" : owner;
        owner = owner.contains("Leopold TSOGO") ? "Léopold TSOGO" : owner;
        return owner;
    }

    private void setActivityTypeContext(Activity activity, User user, Action.ActionBuilder builder) {
        long activityDuration = getActivityDuration(activity);
        switch (activity.getType()) {
            case COACHING_HORS_OPPORTUNITE:
                builder.type(ActionType.COACHING);
                builder.context(String.format("Coaching de %s hors opportunité", activity.getRelatedTo()));
                break;
            case COACHING_SUR_OPPORTUNITE:
                builder.type(ActionType.COACHING);
                builder.context(String.format("Coaching de %s sur opportunité", activity.getRelatedTo()));
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
            case CREATION_DE_SUPPORT_DE_FORMATION_2H_HTT:
                builder.type(ActionType.TWO_HOURS_HTT_TRAINING_SUPPORT);
                builder.context(String.format("Support Formation 2H HTT : %s", activity.getComment()));
                break;
            case CREATION_DE_SUPPORT_DE_FORMATION_1J:
                builder.type(ActionType.ONE_DAY_TRAINING_SUPPORT);
                builder.context(String.format("Support Formation 1J : %s", activity.getComment()));
                break;
            case CREATION_DE_SUPPORT_DE_FORMATION_1J_HTT:
                builder.type(ActionType.ONE_DAY_HTT_TRAINING_SUPPORT);
                builder.context(String.format("Support Formation 1J HTT : %s", activity.getComment()));
                break;
            case ANIMATION_SESSION_DE_FORMATION_2H_HTT:
                builder.type(ActionType.TWO_HOUR_HTT_FORMATION_ANIMATION);
                builder.context(String.format("Animation Formation 2H HTT : %s", activity.getComment()));
                break;
            case ANIMATION_SESSION_DE_FORMATION_1J:
                builder.type(ActionType.ONE_DAY_FORMATION_ANIMATION);
                builder.context(String.format("Animation Formation 1J : %s", activity.getComment()));
                break;
            case ANIMATION_DUNE_SESSION_DE_FORMATION_AU_FORMAT_WEBINAR:
                builder.type(ActionType.WEBINAR);
                builder.context(String.format("Animation Formation Webinar : %s", activity.getComment()));
                break;
            case PARTICIPATION_A_UNE_CONFERENCE_EN_TANT_QUE_SPEAKER:
                builder.type(ActionType.SPEAKER);
                builder.context(String.format("Conférence : %s %s", activity.getComment(), activity.getOpportunity()));
                break;
            case RETEX:
                builder.type(ActionType.RETEX);
                builder.context(String.format("Retex : %s", activity.getComment()));
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
            case INTERVIEW:
                builder.type(ActionType.INTERVIEW);
                builder.context(String.format("Interview Collaborateur : %s", activity.getComment()));
                break;
            case AUDIT_CIR_PHASE_1:
                builder.type(ActionType.AUDIT_CIR_PHASE_1);
                builder.context(String.format(
                        "Audit R&D - phase 1 : %s\nRéponse à un questionnaire et éventuellement entretien téléphonique pour valider la "
                        + "compréhension des questions",
                        activity.getComment()));
                break;
            case AUDIT_CIR_PHASE_2:
                builder.type(ActionType.AUDIT_CIR_PHASE_2);
                builder.context(String.format(
                        "Audit R&D - phase 2 : %s\nRelecture de la partie concernant l’intervenant sur le rapport technique",
                        activity.getComment()));
                break;
            case DEV_SMALL:
                builder.type(ActionType.PROJET_FIL_ROUGE_TICKET_S);
                builder.context(String.format("Projet fil rouge - ticket S : %s", activity.getComment()));
                break;
            case DEV_MEDIUM:
                builder.type(ActionType.PROJET_FIL_ROUGE_TICKET_M);
                builder.context(String.format("Projet fil rouge - ticket M : %s", activity.getComment()));
                break;
            case DEV_LARGE:
                builder.type(ActionType.PROJET_FIL_ROUGE_TICKET_L);
                builder.context(String.format("Projet fil rouge - ticket L : %s", activity.getComment()));
                break;
            case DEV_EXTRA_LARGE:
                builder.type(ActionType.PROJET_FIL_ROUGE_TICKET_XL);
                builder.context(String.format("Projet fil rouge - ticket XL : %s", activity.getComment()));
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
