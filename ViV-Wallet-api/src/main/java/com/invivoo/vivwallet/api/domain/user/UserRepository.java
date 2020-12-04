package com.invivoo.vivwallet.api.domain.user;


import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullNameIgnoreCase(String owner);

    Optional<User> findByX4bId(String x4bId);

    List<User> findByExpertisesExpertise(Expertise expertise);

}
