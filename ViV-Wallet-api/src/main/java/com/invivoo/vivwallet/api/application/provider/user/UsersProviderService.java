package com.invivoo.vivwallet.api.application.provider.user;

import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UsersProviderService {

    public static final int USER_SHEET_INDEX = 0;
    public static final int START_ROW = 2;

    private final UserService userService;

    @Autowired
    public UsersProviderService(UserService userService,
                                ActionService actionService,
                                PaymentService paymentService) {
        this.userService = userService;
    }

    public List<User> importUserFromExcel(XSSFWorkbook workbook) {
        List<User> users = new ArrayList<>();
        for (Row row : workbook.getSheetAt(USER_SHEET_INDEX)) {
            if (row.getRowNum() < START_ROW) {
                continue;
            }
            String fullName = UsersProviderExcelCellResolver.FULL_NAME.resolve(row);
            if (StringUtils.isBlank(fullName)) {
                break;
            }
            User.UserBuilder userBuilder = userService.findByFullName(fullName)
                                                      .stream()
                                                      .peek(user -> Optional.ofNullable(user.getRoles()).ifPresent(Set::clear))
                                                      .peek(user -> Optional.ofNullable(user.getRoles()).ifPresent(Set::clear))
                                                      .peek(userService::save)
                                                      .map(User::toBuilder)
                                                      .findFirst()
                                                      .orElse(User.builder().fullName(fullName));
            RoleType.forName(UsersProviderExcelCellResolver.ROLE_TYPE.resolve(row))
                    .map(Role::new)
                    .map(Set::of)
                    .ifPresent(userBuilder::roles);
            UserExpertiseStatus expertiseStatus = UserExpertiseStatus.forName(UsersProviderExcelCellResolver.EXPERTISE_STATUS.resolve(row))
                                                                     .orElse(null);
            LocalDate startDate = Optional.ofNullable(UsersProviderExcelCellResolver.GT_ENTRY_DATE.resolve(row))
                                          .map(Date.class::cast)
                                          .map(this::convertToLocalDate)
                                          .orElse(null);
            HashSet<UserExpertise> userExpertises = new HashSet<>();
            Expertise.forName(UsersProviderExcelCellResolver.EXPERTISE_1.resolve(row))
                     .map(expertise -> createUserExpertise(expertiseStatus, startDate, expertise))
                     .ifPresent(userExpertises::add);
            Expertise.forName(UsersProviderExcelCellResolver.EXPERTISE_2.resolve(row))
                     .map(expertise -> createUserExpertise(expertiseStatus, startDate, expertise))
                     .ifPresent(userExpertises::add);
            userBuilder.expertises(userExpertises);
            users.add(userBuilder.build());
        }
        return userService.saveAll(users);
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }

    private UserExpertise createUserExpertise(UserExpertiseStatus expertiseStatus, LocalDate startDate, Expertise expertise) {
        return UserExpertise.builder()
                            .expertise(expertise)
                            .startDate(startDate)
                            .status(expertiseStatus)
                            .build();
    }

}
