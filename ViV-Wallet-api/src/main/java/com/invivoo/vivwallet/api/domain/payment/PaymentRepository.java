package com.invivoo.vivwallet.api.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByReceiverOrderByDateDesc(String fullName);
}
