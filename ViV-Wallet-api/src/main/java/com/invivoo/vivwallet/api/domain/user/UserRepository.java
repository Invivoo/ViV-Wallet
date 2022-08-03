package com.invivoo.vivwallet.api.domain.user;


import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE upper(user.fullName) = upper(trim(:fullName))")
    Optional<User> findByFullNameTrimIgnoreCase(@Param("fullName") String fullName);

    Optional<User> findByFullNameIgnoreCase(String owner);

    Optional<User> findByX4bIdIgnoreCase(String x4bId);

    List<User> findDistinctByExpertisesExpertise(Expertise expertise);

    Optional<User> findFirstByRolesType(RoleType type);

    List<User> findByEmailNotNullAndIn(List<String> emails);
}
