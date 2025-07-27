package com.kshitij.mytutor.service.impl;

import com.kshitij.mytutor.dto.AuthResponse;
import com.kshitij.mytutor.dto.LoginRequest;
import com.kshitij.mytutor.dto.RegisterRequest;
import com.kshitij.mytutor.model.User;
import com.kshitij.mytutor.repository.UserRepository;
import com.kshitij.mytutor.security.AppUserDetailsService;
import com.kshitij.mytutor.security.JwtService;
import com.kshitij.mytutor.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final AppUserDetailsService appUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
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
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(),
                        request.password()
                )
        );

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.usernameOrEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(jwtToken, refreshToken);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }
}
