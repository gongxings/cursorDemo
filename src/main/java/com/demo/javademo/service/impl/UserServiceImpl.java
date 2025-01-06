package com.demo.javademo.service.impl;

import com.demo.javademo.entity.User;
import com.demo.javademo.mapper.UserMapper;
import com.demo.javademo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    @Transactional
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setId(id);
        userMapper.update(user);
        return userMapper.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userMapper.delete(id);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("defaultPassword")); // 设置默认密码
        userMapper.updatePassword(id, user.getPassword());
    }
} 