package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Table(name = "actions", uniqueConstraints = @UniqueConstraint(columnNames = {"lynxActivityId", "achiever_id", "type"}))
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Action {

    public static final Long ACTION_FOR_INITIAL_BALANCE_ID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private LocalDateTime valueDate;
    private ActionType type;
    private Long lynxActivityId;
    private int vivAmount;
    private String context;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private User achiever;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private User creator;
    @Enumerated(EnumType.STRING)
    private ActionStatus status;
    private boolean isDeleted;

}
