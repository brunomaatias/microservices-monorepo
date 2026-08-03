package com.portfolio.fsm.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid_role", unique = true)
    private UUID uuidRole = UUID.randomUUID();

    @Column(name = "name")
    private String name;

    private Boolean active;

    @Column(name = "inactive_reason")
    private String inactiveReason;

    @Column(name = "notes")
    private String notes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

    @Override
    public String getAuthority() {
        return name;
    }
}
