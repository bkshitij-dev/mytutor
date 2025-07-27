package com.kshitij.mytutor.service;

import com.kshitij.mytutor.dto.AuthResponse;
import com.kshitij.mytutor.dto.LoginRequest;
import com.kshitij.mytutor.dto.RegisterRequest;
import com.kshitij.mytutor.model.User;

import java.util.Optional;

/**
 * @author Kshitij
 * @created 26-Jul-2025
 */

public interface AuthenticationService {

    User register(RegisterRequest request) throws Exception;

    AuthResponse login(LoginRequest request);

    Optional<User> findByUsernameOrEmail(String username, String email);

}
