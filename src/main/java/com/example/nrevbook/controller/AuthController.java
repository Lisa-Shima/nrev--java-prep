// src/main/java/com/example/nrevbook/controller/AuthController.java
package com.example.nrevbook.controller;

import com.example.nrevbook.dto.AuthResponse;
import com.example.nrevbook.dto.LoginRequest;
import com.example.nrevbook.dto.RegisterRequest;
import com.example.nrevbook.model.Role;
import com.example.nrevbook.model.User;
import com.example.nrevbook.repository.UserRepository;
import com.example.nrevbook.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username is already taken");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(
                req.getUsername(), req.getPassword());
        var auth = authenticationManager.authenticate(authToken);

        String jwt = jwtProvider.generateToken(req.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

}
