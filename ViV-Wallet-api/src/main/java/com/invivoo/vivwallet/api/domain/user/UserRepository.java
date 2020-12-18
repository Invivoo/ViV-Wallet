package com.invivoo.vivwallet.api.domain.user;


import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullNameIgnoreCase(String owner);

    Optional<User> findByX4bId(String x4bId);

    List<User> findDistinctByExpertisesExpertise(Expertise expertise);

    Optional<User> findFirstByRolesType(RoleType type);

}
