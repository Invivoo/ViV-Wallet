package com.invivoo.vivwallet.api.domain.expertise;

import com.invivoo.vivwallet.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpertiseMemberRepository extends JpaRepository<ExpertiseMember, Long> {
    List<ExpertiseMember> findByUser(User user);
}
