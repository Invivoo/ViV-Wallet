package com.invivoo.vivwallet.api.interfaces.users;

import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.interfaces.expertises.ExpertiseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private ExpertiseDto expertise;
    private UserExpertiseStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RoleType> roles = new ArrayList<>();

    public static UserDto createFromUser(User user) {
        UserDtoBuilder builder = UserDto.builder()
                                        .id(user.getId())
                                        .fullName(user.getFullName())
                                        .email(user.getEmail());
        Optional.ofNullable(user.getRoles())
                .ifPresent(roles -> builder.roles(user.getRoles().stream().map(Role::getType).collect(Collectors.toList())));
        LocalDate now = LocalDate.now();
        Optional<UserExpertise> firstActiveExpertise = Optional.ofNullable(user.getExpertises())
                                                               .flatMap(userExpertises -> userExpertises.stream()
                                                                                                        .filter(userExpertise -> userExpertise.isValid(now))
                                                                                                        .max(Comparator.naturalOrder()));
        firstActiveExpertise.ifPresent(activeExpertise -> builder.expertise(ExpertiseDto.fromExpertise(activeExpertise.getExpertise()))
                                                                 .status(activeExpertise.getStatus())
                                                                 .startDate(activeExpertise.getStartDate())
                                                                 .endDate(activeExpertise.getEndDate()));
        return builder.build();
    }

}
