package com.demodd.application.iam.user.dto;

public record AuthTokenResponse(
        String token,
        String tokenType,
        long expiresInSeconds,
        UserResponse user
) {
    public static AuthTokenResponse of(String token, long expiresInSeconds, UserResponse user) {
        return new AuthTokenResponse(token, "Bearer", expiresInSeconds, user);
    }
}
