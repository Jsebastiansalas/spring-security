package com.demodd.domain.iam.user.model.valueobject;

import com.demodd.domain.common.ValueObject;

import java.util.Objects;

public class PasswordHash implements ValueObject {

    private final String value;

    public PasswordHash(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El hash de contrasena no puede estar vacio");
        }
        this.value = value;
    }

    public static PasswordHash of(String value) {
        return new PasswordHash(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHash that = (PasswordHash) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "******";
    }
}
