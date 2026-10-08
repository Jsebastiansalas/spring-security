package com.demodd.domain.iam.user.event;

import com.demodd.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(UUID aggregateId, String username, String email, Instant occurredOn) implements DomainEvent {

    public UserRegisteredEvent(UUID aggregateId, String username, String email) {
        this(aggregateId, username, email, Instant.now());
    }

    @Override
    public String eventType() {
        return "iam.user.registered";
    }
}
