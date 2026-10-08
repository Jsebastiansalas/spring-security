package com.demodd.domain.iam.user.exception;

import com.demodd.domain.common.DomainException;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
