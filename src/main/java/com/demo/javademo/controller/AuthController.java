package com.demo.javademo.controller;

import com.demo.javademo.dto.LoginRequest;
import com.demo.javademo.dto.LoginResponse;
import com.demo.javademo.dto.UserInfoResponse;
import com.demo.javademo.service.UserService;
import com.demo.javademo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Result<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(Result.success(response.getMessage(), response));
        } else {
            return ResponseEntity.ok(Result.error(response.getMessage()));
        }
    }

    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = userService.getCurrentUserInfo();
        return Result.success(userInfo);
    }
} 