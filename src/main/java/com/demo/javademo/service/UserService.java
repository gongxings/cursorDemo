package com.demo.javademo.service;

import com.demo.javademo.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
    void resetPassword(Long id);
} 