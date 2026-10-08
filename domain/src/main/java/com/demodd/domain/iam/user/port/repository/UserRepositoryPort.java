package com.demodd.domain.iam.user.port.repository;

import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.UserEmail;
import com.demodd.domain.iam.user.model.valueobject.UserId;
import com.demodd.domain.iam.user.model.valueobject.Username;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(Username username);

    Optional<User> findByEmail(UserEmail email);

    boolean existsById(UserId id);

    boolean existsByUsername(Username username);

    boolean existsByEmail(UserEmail email);

    List<User> findAll();

    void deleteById(UserId id);
}
