package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.LynxUser;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Service
public class LynxUserToUserMapper {

    private static final Pattern GROW_TOGETHER_POSITION_EXTRACT_PATTERN = Pattern.compile("^([a-zA-Z/\\d]+) [a-zA-Z]+@[a-zA-Z]+ ([a-zA-Z ]+)\\[(.*[^]])");
    public static final int GROW_TOGETHER_POSITION_STATUS_GROUP = 1;
    public static final int GROW_TOGETHER_POSITION_EXPERTISE_EXTRACT_PATTERN = 2;
    public static final int GROW_TOGETHER_POSITION_START_DATE_GROUP = 3;
    public static final int CUSTOMER_TEAM_INCLUDED_START_CHARACTER = 0;
    public static final int CUSTOMER_TEAM_EXCLUDED_END_CHARACTER = 2;
    public static final int DEFAULT_VIV_INITIAL_BALANCE = 0;
    public static final LocalDateTime DEFAULT_VIV_INITIAL_BALANCE_DATE = LocalDateTime.now();


    public User convert(LynxUser lynxUser) {
        User.UserBuilder userBuilder = User.builder();
        provideFullName(lynxUser, userBuilder);
        // todo userBuilder.email(lynxUser.get???)
        provideUserExpertiseStatus(lynxUser, userBuilder);
        provideRoles(lynxUser, userBuilder);
        provideVivInitialBalanceInformation(userBuilder);
        return userBuilder.build();
    }

    private User.UserBuilder provideFullName(LynxUser lynxUser, User.UserBuilder userBuilder) {
        return userBuilder.fullName(String.format("%s %s", StringUtils.capitalize(lynxUser.getCandFirstname()), lynxUser.getCandLastname()));
    }


    private void provideUserExpertiseStatus(LynxUser lynxUser, User.UserBuilder userBuilder) {
        getUserExpertise(lynxUser)
                .map(Collections::singleton)
                .ifPresent(userBuilder::expertises);
    }

    private Optional<UserExpertise> getUserExpertise(LynxUser lynxUser) {
        Matcher growTogetherPositionMatcher = GROW_TOGETHER_POSITION_EXTRACT_PATTERN.matcher(lynxUser.getInvGtPosition());
        return Expertise.forName(growTogetherPositionMatcher.group(GROW_TOGETHER_POSITION_EXPERTISE_EXTRACT_PATTERN))
                .map(expertise -> getUserExpertise(growTogetherPositionMatcher, expertise));
    }

    private UserExpertise getUserExpertise(Matcher growTogetherPositionMatcher, Expertise expertise) {
        return UserExpertise.builder()
                .expertise(expertise)
                .status(getStatus(growTogetherPositionMatcher.group(GROW_TOGETHER_POSITION_STATUS_GROUP)))
                .startDate(LocalDate.parse(growTogetherPositionMatcher.group(GROW_TOGETHER_POSITION_START_DATE_GROUP), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
    }

    private UserExpertiseStatus getStatus(String growTogetherPositionStatus) {
        switch (growTogetherPositionStatus) {
            case "X/GT/01":
                return UserExpertiseStatus.CONSULTANT;
            case "X/GT/02":
                return UserExpertiseStatus.CONSULTANT_SENIOR;
            case "X/GT/03":
                return UserExpertiseStatus.MANAGER;
            case "X/GT/04":
                return UserExpertiseStatus.MANAGER_SENIOR;
            default:
                return null;
        }
    }

    private void provideRoles(LynxUser lynxUser, User.UserBuilder userBuilder) {
        HashSet<Role> roles = new HashSet<>();
        getUserExpertise(lynxUser).flatMap(this::getRoleType)
                .map(Role::new)
                .ifPresent(roles::add);
        getRoleTypeFromInvivooCustomerTeam(lynxUser).map(Role::new)
                .ifPresent(roles::add);
        userBuilder.roles(roles);
    }

    private Optional<RoleType> getRoleTypeFromInvivooCustomerTeam(LynxUser lynxUser) {
        switch (lynxUser.getInvCustomerTeam().substring(CUSTOMER_TEAM_INCLUDED_START_CHARACTER, CUSTOMER_TEAM_EXCLUDED_END_CHARACTER)) {
            case "XC":
                return Optional.of(RoleType.SOFTWARE);
            case "GT":
                return Optional.of(RoleType.CONSULTANT);
            default:
                return Optional.empty();
        }
    }

    private Optional<RoleType> getRoleType(UserExpertise userExpertise) {
        switch (userExpertise.getStatus()) {
            case MANAGER:
                return Optional.of(RoleType.EXPERTISE_MANAGER);
            case MANAGER_SENIOR:
                return Optional.of(RoleType.SENIOR_MANAGER);
            default:
                return Optional.empty();
        }
    }

    private void provideVivInitialBalanceInformation(User.UserBuilder userBuilder) {
        userBuilder.vivInitialBalance(DEFAULT_VIV_INITIAL_BALANCE);
        userBuilder.vivInitialBalanceDate(DEFAULT_VIV_INITIAL_BALANCE_DATE);
    }

}
