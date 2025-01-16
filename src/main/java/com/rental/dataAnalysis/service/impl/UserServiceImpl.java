package com.rental.dataAnalysis.service.impl;

import com.rental.dataAnalysis.constant.RoleConstants;
import com.rental.dataAnalysis.dto.LoginRequest;
import com.rental.dataAnalysis.dto.LoginResponse;
import com.rental.dataAnalysis.dto.RegisterRequest;
import com.rental.dataAnalysis.dto.UserInfoResponse;
import com.rental.dataAnalysis.entity.User;
import com.rental.dataAnalysis.mapper.UserMapper;
import com.rental.dataAnalysis.security.JwtUtils;
import com.rental.dataAnalysis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 使用 JWT 生成 token
            String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

            return LoginResponse.builder()
                    .success(true)
                    .message("登录成功")
                    .token(token)
                    .build();
        } catch (Exception e) {
            return LoginResponse.builder()
                    .success(false)
                    .message("用户名或密码错误")
                    .build();
        }
    }

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRole(RoleConstants.ROLE_USER);
        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userMapper.findByUsername(authentication.getName());
    }

    @Override
    @Transactional
    public User updateProfile(User user) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !currentUser.getId().equals(user.getId())) {
            throw new RuntimeException("无权限修改其他用户信息");
        }

        // 只允许修改邮箱和电话
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());

        userMapper.update(updateUser);
        return userMapper.findById(user.getId());
    }

    @Override
    @Transactional
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // 更新密码
        userMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.findAll();
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Long userId, Integer status) {
        return userMapper.updateStatus(userId, status) > 0;
    }

    @Override
    public UserInfoResponse getCurrentUserInfo() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        return UserInfoResponse.builder()
                .username(currentUser.getUsername())
                .role(currentUser.getRole())
                .permissions(Arrays.asList("house:view", "house:edit", "analysis:view"))  // 这里可以根据实际权限配置
                .avatar("https://avatars.githubusercontent.com/u/1")  // 这里可以是实际的头像URL
                .email(currentUser.getEmail())
                .build();
    }

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
        user.setStatus(1);
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
        user.setPassword(passwordEncoder.encode("123456")); // 设置默认密码
        userMapper.updatePassword(id, user.getPassword());
    }

    @Override
    @Transactional
    public void updateRole(Long userId, String role) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setRole(role);
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
    }

    @Override
    public void invalidateToken(String token) {
        // 从 UserToken 表中删除 token
        userMapper.deleteToken(token);
    }
} 