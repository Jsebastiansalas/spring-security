package com.demodd.domain.iam.user.exception;

import com.demodd.domain.common.DomainException;
import com.demodd.domain.iam.user.model.valueobject.UserId;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(UserId id) {
        super("No se encontro el usuario con identificador: " + id.value());
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
