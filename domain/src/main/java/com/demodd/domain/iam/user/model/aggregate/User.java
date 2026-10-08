package com.demodd.domain.iam.user.model.aggregate;

import com.demodd.domain.common.AggregateRoot;
import com.demodd.domain.iam.user.event.UserPasswordChangedEvent;
import com.demodd.domain.iam.user.event.UserRegisteredEvent;
import com.demodd.domain.iam.user.model.valueobject.*;

import java.time.Instant;
import java.util.Objects;

public class User extends AggregateRoot<UserId> {

    private final UserId id;
    private Username username;
    private UserEmail email;
    private PasswordHash passwordHash;
    private UserRole role;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public User(UserId id, Username username, UserEmail email, PasswordHash passwordHash,
                UserRole role, UserStatus status, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "El identificador no puede ser nulo");
        this.username = Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo");
        this.email = Objects.requireNonNull(email, "El correo electronico no puede ser nulo");
        this.passwordHash = Objects.requireNonNull(passwordHash, "El hash de contrasena no puede ser nulo");
        this.role = role != null ? role : UserRole.defaultRole();
        this.status = status != null ? status : UserStatus.active();
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public static User create(Username username, UserEmail email, PasswordHash passwordHash, UserRole role) {
        UserId id = UserId.random();
        User user = new User(
                id,
                username,
                email,
                passwordHash,
                role != null ? role : UserRole.defaultRole(),
                UserStatus.active(),
                Instant.now(),
                Instant.now()
        );
        user.recordEvent(new UserRegisteredEvent(id.value(), username.value(), email.value(), user.getCreatedAt()));
        return user;
    }

    public void changePassword(PasswordHash newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "La nueva contrasena no puede ser nula");
        this.updatedAt = Instant.now();
        recordEvent(new UserPasswordChangedEvent(this.id.value(), this.updatedAt));
    }

    public void updateRole(UserRole role) {
        this.role = Objects.requireNonNull(role, "El rol no puede ser nulo");
        this.updatedAt = Instant.now();
    }

    public void updateStatus(UserStatus status) {
        this.status = Objects.requireNonNull(status, "El estado no puede ser nulo");
        this.updatedAt = Instant.now();
    }

    @Override
    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public UserEmail getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
