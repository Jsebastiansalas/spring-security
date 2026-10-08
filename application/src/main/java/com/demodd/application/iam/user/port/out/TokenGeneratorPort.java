package com.demodd.application.iam.user.port.out;

import com.demodd.domain.iam.user.model.aggregate.User;

public interface TokenGeneratorPort {

    String generateToken(User user);

    long getExpirationInSeconds();
}
