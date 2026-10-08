package com.demodd.application.iam.user.dto;

import com.demodd.domain.iam.user.model.aggregate.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String role,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId().value(),
                user.getUsername().value(),
                user.getEmail().value(),
                user.getRole().value(),
                user.getStatus().value(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
