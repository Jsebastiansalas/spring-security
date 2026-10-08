package com.demodd.infrastructure.security.service;

import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.Username;
import com.demodd.domain.iam.user.port.repository.UserRepositoryPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepository;

    public CustomUserDetailsService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(Username.of(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername().value(),
                user.getPasswordHash().value(),
                user.getStatus().isActive(),
                true,
                true,
                !user.getStatus().value().equalsIgnoreCase("LOCKED"),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().value()))
        );
    }
}
