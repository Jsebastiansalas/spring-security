package com.demodd.application.iam.user.dto;

public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String role
) {}
