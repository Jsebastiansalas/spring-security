package com.demodd.application.iam.user.dto;

public record LoginCommand(
        String username,
        String password
) {}
