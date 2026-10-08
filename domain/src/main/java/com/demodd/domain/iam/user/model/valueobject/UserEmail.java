package com.demodd.domain.iam.user.model.valueobject;

import com.demodd.domain.common.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserEmail implements ValueObject {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final String value;

    public UserEmail(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electronico no puede estar vacio");
        }
        String trimmed = value.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("El formato del correo electronico no es valido: " + value);
        }
        this.value = trimmed;
    }

    public static UserEmail of(String value) {
        return new UserEmail(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(value, userEmail.value);
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
