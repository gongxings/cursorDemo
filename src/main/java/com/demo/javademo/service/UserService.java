package com.demo.javademo.service;

import com.demo.javademo.dto.LoginRequest;
import com.demo.javademo.dto.LoginResponse;
import com.demo.javademo.dto.RegisterRequest;
import com.demo.javademo.dto.UserInfoResponse;
import com.demo.javademo.entity.User;
import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    
    User register(RegisterRequest registerRequest);
    
    UserInfoResponse getCurrentUserInfo();
    
    User getCurrentUser();
    
    User updateProfile(User user);
    
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
    
    List<User> findAllUsers();
    
    boolean updateUserStatus(Long userId, Integer status);
} 