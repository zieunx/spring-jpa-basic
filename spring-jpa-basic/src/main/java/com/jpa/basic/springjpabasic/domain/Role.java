package com.jpa.basic.springjpabasic.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    private String id;

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "role_perm",
            joinColumns = @JoinColumn(name = "role_id")
    )
    private Set<GrantedPermission> permissions = new HashSet<>();

    public Role() {
    }

    public Role(String id, String name, Set<GrantedPermission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Set<GrantedPermission> getPermissions() {
        return permissions;
    }
}
