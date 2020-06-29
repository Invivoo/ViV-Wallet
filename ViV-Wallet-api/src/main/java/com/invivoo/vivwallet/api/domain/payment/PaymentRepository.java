package com.invivoo.vivwallet.api.domain.payment;

import com.invivoo.vivwallet.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> getAllByOrderByDateDesc();

    List<Payment> findAllByReceiverOrderByDateDesc(User receiver);
}
