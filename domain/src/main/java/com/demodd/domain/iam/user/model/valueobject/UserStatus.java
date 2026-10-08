package com.demodd.domain.iam.user.model.valueobject;

import com.demodd.domain.common.ValueObject;

import java.util.Objects;

public class UserStatus implements ValueObject {

    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String LOCKED = "LOCKED";

    private final String value;

    public UserStatus(String value) {
        this.value = value != null ? value.trim().toUpperCase() : ACTIVE;
    }

    public static UserStatus of(String value) {
        return new UserStatus(value);
    }

    public static UserStatus active() {
        return new UserStatus(ACTIVE);
    }

    public static UserStatus inactive() {
        return new UserStatus(INACTIVE);
    }

    public static UserStatus locked() {
        return new UserStatus(LOCKED);
    }

    public boolean isActive() {
        return ACTIVE.equalsIgnoreCase(this.value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatus that = (UserStatus) o;
        return Objects.equals(value, that.value);
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
