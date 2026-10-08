package com.demodd.application.iam.user.port.in;

import com.demodd.application.iam.user.dto.UserResponse;

public interface GetUserByUsernameUseCase {

    UserResponse execute(String username);
}
