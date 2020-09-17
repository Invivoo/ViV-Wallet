package com.invivoo.vivwallet.api.interfaces.expertise_members;

import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertiseMemberUpdateDTO {
    private ExpertiseMemberStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
