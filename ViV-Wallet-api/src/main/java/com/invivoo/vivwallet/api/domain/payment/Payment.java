package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Data
@ToString(exclude = "actions")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @ManyToOne
    private User receiver;
    @ManyToOne
    private User creator;
    private int vivAmount;
    @OneToMany(mappedBy = "payment")
    private Collection<Action> actions;
}
