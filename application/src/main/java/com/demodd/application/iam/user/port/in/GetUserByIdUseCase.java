package com.demodd.application.iam.user.port.in;

import com.demodd.application.iam.user.dto.UserResponse;

import java.util.UUID;

public interface GetUserByIdUseCase {

    UserResponse execute(UUID id);
}
