package com.rental.dataAnalysis.controller;

import com.rental.dataAnalysis.dto.LoginRequest;
import com.rental.dataAnalysis.dto.LoginResponse;
import com.rental.dataAnalysis.dto.UserInfoResponse;
import com.rental.dataAnalysis.service.UserService;
import com.rental.dataAnalysis.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(response.getMessage()));
        }
    }

    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        UserInfoResponse userInfo = userService.getCurrentUserInfo();
        return Result.success(userInfo);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userService.invalidateToken(token);
        }
        return Result.success("退出登录成功");
    }
} 