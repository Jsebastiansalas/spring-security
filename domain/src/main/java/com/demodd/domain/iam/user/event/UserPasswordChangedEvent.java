package com.demodd.domain.iam.user.event;

import com.demodd.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record UserPasswordChangedEvent(UUID aggregateId, Instant occurredOn) implements DomainEvent {

    public UserPasswordChangedEvent(UUID aggregateId) {
        this(aggregateId, Instant.now());
    }

    @Override
    public String eventType() {
        return "iam.user.password_changed";
    }
}
