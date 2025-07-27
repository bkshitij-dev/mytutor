package com.kshitij.mytutor.controller;

import com.kshitij.mytutor.dto.AuthResponse;
import com.kshitij.mytutor.dto.LoginRequest;
import com.kshitij.mytutor.dto.RefreshTokenRequest;
import com.kshitij.mytutor.dto.RegisterRequest;
import com.kshitij.mytutor.security.AppUserDetailsService;
import com.kshitij.mytutor.security.JwtService;
import com.kshitij.mytutor.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kshitij
 * @created 26-Jul-2025
 */

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AppUserDetailsService appUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authenticationService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(),
                        request.password()
                )
        );

        UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.usernameOrEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken, refreshToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.refreshToken());
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(request.refreshToken(), userDetails)) {
            String newAccessToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken()));
        }

        return ResponseEntity.badRequest().build();
    }
}
