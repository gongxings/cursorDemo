package com.rental.dataAnalysis.entity;

import lombok.Data;

@Data
public class UserRole {

    private Long id;

    private User userId;

    private Role roleId;
} 