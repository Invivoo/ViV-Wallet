package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {

    List<Action> findAllByOrderByDateDesc();

    List<Action> findAllByAchieverAndDateAfter(User achiever, LocalDateTime date);

    List<Action> findAllByPaymentIdOrderByDateDesc(@Param("paymentId") Long paymentId);

    List<Action> findAllByAchieverOrderByDateDesc(User achiever);

    List<Action> findAllByAchieverAndPaymentIsNullOrderByDateAsc(User achiever);

    List<Action> findAllByLynxActivityIdInAndPaymentIsNotNull(List<Long> lynxActivityId);

    List<Action> deleteAllByPaymentIsNull();
}
