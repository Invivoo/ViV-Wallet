package com.invivoo.vivwallet.api.interfaces.users.expertises;

import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.interfaces.expertises.ExpertiseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserExpertiseDto {

    private Long id;
    private ExpertiseDto expertise;
    private UserExpertiseStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public static UserExpertiseDto createFromUserExpertise(UserExpertise userExpertise){
        return UserExpertiseDto.builder()
                        .id(userExpertise.getId())
                        .expertise(ExpertiseDto.fromExpertise(userExpertise.getExpertise()))
                        .status(userExpertise.getStatus())
                        .startDate(userExpertise.getStartDate())
                        .endDate(userExpertise.getEndDate())
                        .build();
    }
}
