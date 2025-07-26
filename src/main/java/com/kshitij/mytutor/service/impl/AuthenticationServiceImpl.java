package com.kshitij.mytutor.service.impl;

import com.kshitij.mytutor.dto.RegisterRequest;
import com.kshitij.mytutor.model.User;
import com.kshitij.mytutor.repository.UserRepository;
import com.kshitij.mytutor.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Kshitij
 * @created 26-Jul-2025
 */

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User register(RegisterRequest request) throws Exception {
        if (findByUsernameOrEmail(request.username(), request.email()).isPresent()) {
            throw new Exception("Username and/or email already exists");
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }
}
