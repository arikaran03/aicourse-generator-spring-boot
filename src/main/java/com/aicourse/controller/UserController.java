package com.aicourse.controller;

import com.aicourse.model.Users;
import com.aicourse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
