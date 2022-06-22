package com.invivoo.vivwallet.api.infrastructure.lynx.mapper;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.LynxUser;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class LynxUserToUserMapperTest {

    private final LynxUserToUserMapper lynxUserToUserMapper = new LynxUserToUserMapper();

    @Test
    public void should_return_valid_user() {
        // GIVEN
        LynxUser lynxUser = LynxUser.builder()
                                    .candFirstname("John")
                                    .candLastname("Doe")
                                    .invCustomerTeam("GT Java")
                                    .invGtPosition("X/GT/01 Consultant@GT Java[24/01/2022]")
                                    .build();
        Role role = Role.builder()
                        .type(RoleType.CONSULTANT)
                        .build();
        UserExpertise userExpertise = UserExpertise.builder()
                                                   .status(UserExpertiseStatus.CONSULTANT)
                                                   .startDate(LocalDate.parse("24/01/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                                   .expertise(Expertise.PROGRAMMATION_JAVA)
                                                   .build();
        User expectedUser = User.builder()
                                .fullName("John Doe")
                                .expertises(Collections.singleton(userExpertise))
                                .vivInitialBalance(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE)
                                .vivInitialBalanceDate(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE_DATE)
                                .roles(Collections.singleton(role))
                                .build();
        role.setUser(expectedUser);
        userExpertise.setUser(expectedUser);

        // WHEN
        User user = lynxUserToUserMapper.convert(lynxUser);

        // THEN
        assertThat(expectedUser).isEqualTo(user);

    }

    @Test
    public void should_return_valid_user_for_big_data() {
        // GIVEN
        LynxUser lynxUser = LynxUser.builder()
                                    .candFirstname("John")
                                    .candLastname("Doe")
                                    .invCustomerTeam("GT Big Data Engineering")
                                    .invGtPosition("X/GT/01 Consultant@GT Big Data Engineering[24/01/2022]")
                                    .build();
        Role role = Role.builder()
                        .type(RoleType.CONSULTANT)
                        .build();
        UserExpertise userExpertise = UserExpertise.builder()
                                                   .status(UserExpertiseStatus.CONSULTANT)
                                                   .startDate(LocalDate.parse("24/01/2022", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                                   .expertise(Expertise.BIG_DATA)
                                                   .build();
        User expectedUser = User.builder()
                                .fullName("John Doe")
                                .expertises(Collections.singleton(userExpertise))
                                .vivInitialBalance(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE)
                                .vivInitialBalanceDate(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE_DATE)
                                .roles(Collections.singleton(role))
                                .build();
        role.setUser(expectedUser);
        userExpertise.setUser(expectedUser);

        // WHEN
        User user = lynxUserToUserMapper.convert(lynxUser);

        // THEN
        assertThat(expectedUser).isEqualTo(user);

    }

    @Test
    public void should_return_valid_user_for_big_data_senior_consultant() {
        // GIVEN
        LynxUser lynxUser = LynxUser.builder()
                                    .candFirstname("John")
                                    .candLastname("Doe")
                                    .invCustomerTeam("GT Big Data Engineering")
                                    .invGtPosition("X/GT/02 Consultant Senior@GT Big Data Engineering[01/07/2021]")
                                    .build();
        Role role = Role.builder()
                        .type(RoleType.CONSULTANT)
                        .build();
        UserExpertise userExpertise = UserExpertise.builder()
                                                   .status(UserExpertiseStatus.CONSULTANT_SENIOR)
                                                   .startDate(LocalDate.parse("01/07/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                                   .expertise(Expertise.BIG_DATA)
                                                   .build();
        User expectedUser = User.builder()
                                .fullName("John Doe")
                                .expertises(Collections.singleton(userExpertise))
                                .vivInitialBalance(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE)
                                .vivInitialBalanceDate(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE_DATE)
                                .roles(Collections.singleton(role))
                                .build();
        role.setUser(expectedUser);
        userExpertise.setUser(expectedUser);

        // WHEN
        User user = lynxUserToUserMapper.convert(lynxUser);

        // THEN
        assertThat(expectedUser).isEqualTo(user);

    }

    @Test
    public void should_return_valid_user_for_unmapped_expertise() {
        // GIVEN
        LynxUser lynxUser = LynxUser.builder()
                                    .candFirstname("John")
                                    .candLastname("Doe")
                                    .invCustomerTeam("GT Big Data Engineering")
                                    .invGtPosition("X/GT/02 Consultant Senior@GT Unmapped Expertise[01/07/2021]")
                                    .build();
        Role role = Role.builder()
                        .type(RoleType.CONSULTANT)
                        .build();
        UserExpertise userExpertise = UserExpertise.builder()
                                                   .status(UserExpertiseStatus.CONSULTANT_SENIOR)
                                                   .startDate(LocalDate.parse("01/07/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                                                   .expertise(Expertise.UNKNOWN_EXPERTISE)
                                                   .build();
        User expectedUser = User.builder()
                                .fullName("John Doe")
                                .expertises(Collections.singleton(userExpertise))
                                .vivInitialBalance(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE)
                                .vivInitialBalanceDate(LynxUserToUserMapper.DEFAULT_VIV_INITIAL_BALANCE_DATE)
                                .roles(Collections.singleton(role))
                                .build();
        role.setUser(expectedUser);
        userExpertise.setUser(expectedUser);

        // WHEN
        User user = lynxUserToUserMapper.convert(lynxUser);

        // THEN
        assertThat(expectedUser).isEqualTo(user);

    }
}
