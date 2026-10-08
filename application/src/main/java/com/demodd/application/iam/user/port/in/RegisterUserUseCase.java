package com.demodd.application.iam.user.port.in;

import com.demodd.application.iam.user.dto.RegisterUserCommand;
import com.demodd.application.iam.user.dto.UserResponse;

public interface RegisterUserUseCase {

    UserResponse execute(RegisterUserCommand command);
}
