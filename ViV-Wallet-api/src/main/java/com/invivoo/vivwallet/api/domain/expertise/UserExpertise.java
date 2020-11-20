package com.invivoo.vivwallet.api.domain.expertise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Comparator;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserExpertise implements Comparable<UserExpertise> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Expertise expertise;
    @Enumerated(EnumType.STRING)
    private UserExpertiseStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    @JsonIgnore
    public boolean isValid(LocalDate date) {
        return (startDate == null || !date.isBefore(startDate))
               && ((endDate == null) || !date.isBefore(endDate));
    }

    @Override
    public int compareTo(UserExpertise o) {
        return Comparator.nullsLast(Comparator.comparing(UserExpertise::getStartDate))
                         .thenComparing(Comparator.nullsFirst(Comparator.comparing(UserExpertise::getEndDate)))
                         .compare(this, o);
    }
}
