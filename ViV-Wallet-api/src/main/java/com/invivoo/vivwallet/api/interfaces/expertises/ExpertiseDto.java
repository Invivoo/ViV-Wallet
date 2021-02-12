package com.invivoo.vivwallet.api.interfaces.expertises;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertiseDto {

    private String id;
    private String expertiseName;

    public static ExpertiseDto fromExpertise(Expertise expertise){
        return new ExpertiseDto(expertise.name(), expertise.getExpertiseName());
    }
}
