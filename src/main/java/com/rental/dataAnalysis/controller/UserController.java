package com.rental.dataAnalysis.controller;

import com.rental.dataAnalysis.entity.User;
import com.rental.dataAnalysis.service.UserService;
import com.rental.dataAnalysis.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(userService.findById(id));
    }

    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        return Result.success(userService.create(user));
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return Result.success(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功");
    }

    @PostMapping("/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.success("密码重置成功");
    }

    @GetMapping("/profile")
    public Result<User> getProfile() {
        Long currentUserId = userService.getCurrentUser().getId();
        return Result.success(userService.findById(currentUserId));
    }
} 