package com.rental.dataAnalysis.service;

import com.rental.dataAnalysis.dto.LoginRequest;
import com.rental.dataAnalysis.dto.LoginResponse;
import com.rental.dataAnalysis.dto.RegisterRequest;
import com.rental.dataAnalysis.dto.UserInfoResponse;
import com.rental.dataAnalysis.entity.User;
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
    void updateRole(Long userId, String role);
    void resetPassword(Long userId, String newPassword);
    void invalidateToken(String token) ;
    List<User> findAll();
    User findById(Long id);
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
    void resetPassword(Long id);
} 