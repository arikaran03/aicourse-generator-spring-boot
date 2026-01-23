package com.aicourse.dto;

import com.aicourse.enums.UserRole;
import com.aicourse.model.Users;

public class UserResponse {

    private Long id;
    private String username;
    private UserRole role;

    public UserResponse(Long id, String username, UserRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
    public UserResponse(Users user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRoles();
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
}
