package com.aicourse.service;

import com.aicourse.dto.LoginResponse;
import com.aicourse.dto.UserResponse;
import com.aicourse.model.Users;
import com.aicourse.repo.UserRepo;
import com.aicourse.service.JWT.JWTService;
import com.aicourse.utils.exception.AuthenticationFailedException;
import com.aicourse.utils.id.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

//    public Users registerUser(Users user){
//        user.setPassword(encoder.encode(user.getPassword()));
//        return userRepo.save(user);
//    }
//
//    public String verify(Users user){
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//        );
//        if(authentication.isAuthenticated()){
//            return jwtService.generateToken(user.getUsername());
//        }
//        return "User not verified";
//    }

        public Users registerUser(Users user){
            user.setId(SnowflakeIdGenerator.generateId());
            user.setPassword(encoder.encode(user.getPassword()));
            // Note: roles and timestamps are now handled automatically in Users.java @PrePersist
            return userRepo.save(user);
        }

        public LoginResponse verify(Users user){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            if(authentication.isAuthenticated()){
                String token = jwtService.generateToken(user.getUsername());
                Users currentUser = userRepo.findByUsername(user.getUsername());
                return new LoginResponse(token, new UserResponse(currentUser));
            }
            else{
                throw new AuthenticationFailedException("User is not verified");
            }
        }
    }