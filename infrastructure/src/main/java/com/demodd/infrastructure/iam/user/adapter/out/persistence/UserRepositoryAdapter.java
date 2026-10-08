package com.demodd.infrastructure.iam.user.adapter.out.persistence;

import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.*;
import com.demodd.domain.iam.user.port.repository.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserSpringDataJpaRepository repository;

    public UserRepositoryAdapter(UserSpringDataJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User entity) {
        UserJpaEntity jpaEntity = new UserJpaEntity(
                entity.getId().value(),
                entity.getUsername().value(),
                entity.getEmail().value(),
                entity.getPasswordHash().value(),
                entity.getRole().value(),
                entity.getStatus().value(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
        UserJpaEntity saved = repository.save(jpaEntity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return repository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return repository.findByUsername(username.value()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(UserEmail email) {
        return repository.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    public boolean existsById(UserId id) {
        return repository.existsById(id.value());
    }

    @Override
    public boolean existsByUsername(Username username) {
        return repository.existsByUsername(username.value());
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return repository.existsByEmail(email.value());
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(UserId id) {
        repository.deleteById(id.value());
    }

    private User toDomain(UserJpaEntity jpa) {
        return new User(
                UserId.of(jpa.getId()),
                Username.of(jpa.getUsername()),
                UserEmail.of(jpa.getEmail()),
                PasswordHash.of(jpa.getPasswordHash()),
                UserRole.of(jpa.getRole()),
                UserStatus.of(jpa.getStatus()),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }
}
