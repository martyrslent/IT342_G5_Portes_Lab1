package com.lab1.backend.controller;

import com.lab1.backend.model.User;
import com.lab1.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. REGISTER
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User registered successfully!");
        
        return ResponseEntity.ok(response);
    }

    // 2. LOGIN (With actual authentication)
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginReq) {
        Map<String, String> response = new HashMap<>();
        
        // Find user by email
        Optional<User> userFound = userRepository.findByEmail(loginReq.getEmail());

        if (userFound.isPresent()) {
            User user = userFound.get();
            // Check if password matches the BCrypt hash
            if (passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
                response.put("status", "success");
                response.put("message", "Login successful!");
                // MUST return a token key so Dashboard doesn't redirect back to Login
                response.put("token", "dummy-jwt-token-for-" + user.getUsername()); 
                return ResponseEntity.ok(response);
            }
        }

        // If user not found or password wrong
        response.put("status", "error");
        response.put("message", "Invalid email or password");
        return ResponseEntity.status(401).body(response);
    }

    // 3. LOGOUT (Needed for the Android button to work)
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    // 4. GET ME
    @GetMapping("/user/me")
    public Object getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal();
    }
}