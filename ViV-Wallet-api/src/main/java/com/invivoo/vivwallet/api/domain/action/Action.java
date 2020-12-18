package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"lynxActivityId", "achiever_id", "type"}))
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private ActionType type;
    private Long lynxActivityId;
    private int vivAmount;
    private String context;
    @ManyToOne
    private User achiever;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Payment payment;
    private boolean isDeleted;
}
