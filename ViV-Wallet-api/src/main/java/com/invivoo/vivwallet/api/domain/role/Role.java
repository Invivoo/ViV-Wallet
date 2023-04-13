package com.invivoo.vivwallet.api.domain.role;


import com.invivoo.vivwallet.api.domain.user.User;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;

@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}))
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ToString.Exclude
    @HashCodeExclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Enumerated(EnumType.STRING)
    private RoleType type;

    public Role(RoleType type) {
        this.type = type;
    }
}
