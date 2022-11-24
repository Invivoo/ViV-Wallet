package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {

    List<Action> findAllByIsDeletedFalseOrderByDateDesc();

    List<Action> findAllByAchieverAndValueDateAfterAndStatus(User achiever, LocalDateTime date, ActionStatus status);

    List<Action> findAllByAchieverAndValueDateAfter(User achiever, LocalDateTime date);

    List<Action> findAllByLynxActivityIdIn(List<Long> lynxActivityId);

    List<Action> deleteAllByStatus(ActionStatus status);
}
