package com.demo.javademo.entity;

import lombok.Data;

@Data
public class UserRole {

    private Long id;

    private User userId;

    private Role roleId;
} 