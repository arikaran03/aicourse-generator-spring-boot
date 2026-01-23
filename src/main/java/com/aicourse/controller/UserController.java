package com.aicourse.controller;

import com.aicourse.dto.LoginResponse;
import com.aicourse.dto.UserResponse;
import com.aicourse.model.UserPrincipal;
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
    public LoginResponse verifyUser(@RequestBody Users user) {
        return service.verify(user); // returns JWT
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Users user = principal.getUser();

        return ResponseEntity.ok(
                new UserResponse(user.getId(), user.getUsername(), user.getRoles())
        );
    }
}