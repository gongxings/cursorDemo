package com.rental.dataAnalysis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoResponse {
    private String username;
    private String role;
    private List<String> permissions;
    private String avatar;
    private String email;
} 