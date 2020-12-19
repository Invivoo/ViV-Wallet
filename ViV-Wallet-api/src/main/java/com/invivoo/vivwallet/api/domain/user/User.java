package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@Builder(toBuilder = true, builderClassName = "UserBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String x4bId;
    @Column(unique = true)
    private String fullName;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<UserExpertise> expertises = new HashSet<>();
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Role> roles = new HashSet<>();
    private int vivInitialBalance;
    private LocalDateTime vivInitialBalanceDate;

    public User(Long id, String x4bId, String fullName, Set<UserExpertise> expertises, Set<Role> roles) {
        this.id = id;
        this.x4bId = x4bId;
        this.fullName = fullName;
        this.expertises = expertises;
        this.roles = roles;
    }
}
