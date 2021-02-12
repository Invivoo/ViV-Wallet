package com.invivoo.vivwallet.api.domain.expertise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.invivoo.vivwallet.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Comparator;

@Table(name = "user_expertises")
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserExpertise implements Comparable<UserExpertise> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
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
        return Comparator.comparing(UserExpertise::getStartDate, Comparator.nullsLast(Comparator.naturalOrder()))
                         .thenComparing(UserExpertise::getEndDate, Comparator.nullsLast(Comparator.naturalOrder()))
                         .compare(this, o);
    }
}
