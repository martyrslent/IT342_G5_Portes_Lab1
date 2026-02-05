package com.lab1.backend.controller;

import com.lab1.backend.model.User;
import com.lab1.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. REGISTER
    @PostMapping("/auth/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // BCrypt Encryption
        userRepository.save(user);
        return "User registered successfully!";
    }

    // 2. LOGIN (Simplified for Lab 1)
    @PostMapping("/auth/login")
    public String login(@RequestBody User user) {
        return "Login successful!"; 
    }

    // 3. GET ME (Protected)
    @GetMapping("/user/me")
    public Object getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal(); 
    }
}