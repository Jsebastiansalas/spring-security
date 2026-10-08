package com.demodd.domain.iam.user.model.valueobject;

import com.demodd.domain.common.ValueObject;

import java.util.Objects;
import java.util.Set;

public class UserRole implements ValueObject {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String PROFESSIONAL = "ROLE_PROFESSIONAL";
    public static final String PATIENT = "ROLE_PATIENT";
    public static final String USER = "ROLE_USER";

    private static final Set<String> ALLOWED_ROLES = Set.of(ADMIN, PROFESSIONAL, PATIENT, USER);

    private final String value;

    public UserRole(String value) {
        if (value == null || value.trim().isEmpty()) {
            this.value = USER;
            return;
        }
        String normalized = value.trim().toUpperCase();
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }
        if (!ALLOWED_ROLES.contains(normalized)) {
            throw new IllegalArgumentException("Rol de usuario invalido: " + value);
        }
        this.value = normalized;
    }

    public static UserRole of(String value) {
        return new UserRole(value);
    }

    public static UserRole admin() {
        return new UserRole(ADMIN);
    }

    public static UserRole professional() {
        return new UserRole(PROFESSIONAL);
    }

    public static UserRole patient() {
        return new UserRole(PATIENT);
    }

    public static UserRole defaultRole() {
        return new UserRole(USER);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(value, userRole.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
