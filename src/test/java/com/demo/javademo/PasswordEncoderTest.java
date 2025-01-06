package com.demo.javademo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    
    @Test
    public void generatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("加密后: " + encodedPassword);
        // 验证密码
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("是否匹配: " + matches);
    }
} 