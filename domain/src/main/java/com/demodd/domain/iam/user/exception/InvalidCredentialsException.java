package com.demodd.domain.iam.user.exception;

import com.demodd.domain.common.DomainException;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Credenciales invalidas");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
