package com.aicourse.controller;

import com.aicourse.model.Users;
import com.aicourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user) {
        return service.registerUser(user);
    }

    @PostMapping("/login")
    public String verifyUser(@RequestBody Users user) {
        return service.verify(user); // returns JWT
    }

    // 🔹 Used by frontend to check login status
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}

