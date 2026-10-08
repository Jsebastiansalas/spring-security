package com.demodd.domain.iam.user.model.valueobject;

import com.demodd.domain.common.ValueObject;

import java.util.Objects;

public class Username implements ValueObject {

    private final String value;

    public Username(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacio");
        }
        this.value = value.trim();
    }

    public static Username of(String value) {
        return new Username(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(value, username.value);
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
