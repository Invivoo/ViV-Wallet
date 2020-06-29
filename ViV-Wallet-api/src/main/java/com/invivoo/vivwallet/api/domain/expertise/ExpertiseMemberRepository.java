package com.invivoo.vivwallet.api.domain.expertise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertiseMemberRepository extends JpaRepository<ExpertiseMember, Long> {
    Optional<ExpertiseMember> findByUser(String userName);
}
