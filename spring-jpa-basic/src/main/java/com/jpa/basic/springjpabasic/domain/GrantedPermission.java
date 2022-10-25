package com.jpa.basic.springjpabasic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GrantedPermission {
    @Column(name = "perm")
    private String permission;
    private String grantor;

    protected GrantedPermission() {
    }

    public GrantedPermission(String permission, String grantor) {
        this.permission = permission;
        this.grantor = grantor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantedPermission that = (GrantedPermission) o;

        if (!Objects.equals(permission, that.permission)) return false;
        return Objects.equals(grantor, that.grantor);
    }

    @Override
    public int hashCode() {
        int result = permission != null ? permission.hashCode() : 0;
        result = 31 * result + (grantor != null ? grantor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GrantedPermission{" +
                "permission='" + permission + '\'' +
                ", grantor='" + grantor + '\'' +
                '}';
    }
}
