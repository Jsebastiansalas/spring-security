package com.demodd.application.iam.user.port.in;

import com.demodd.application.iam.user.dto.AuthTokenResponse;
import com.demodd.application.iam.user.dto.LoginCommand;

public interface LoginUseCase {

    AuthTokenResponse execute(LoginCommand command);
}
