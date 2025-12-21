package com.aicourse.controller;

import com.aicourse.model.Users;
import com.aicourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    public UserService service;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user){
        return service.registerUser(user);
    }

    @GetMapping("/")
    public String home(){
        return "hello world";
    }

    @PostMapping("/login")
    public String verifyUser(@RequestBody Users user){
        return service.verify(user);
    }
}
