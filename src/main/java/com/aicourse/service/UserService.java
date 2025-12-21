package com.aicourse.service;

import com.aicourse.model.Users;
import com.aicourse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users registerUser(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
