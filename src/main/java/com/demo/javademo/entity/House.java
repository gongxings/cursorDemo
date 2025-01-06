package com.demo.javademo.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class House {
    private Long id;
    private String title;
    private String address;
    private String district;
    private BigDecimal price;
    private BigDecimal area;
    private Integer roomCount;
    private Integer floor;
    private Integer totalFloor;
    private String buildYear;
    private String type;
    private String status;
    private String features;
    private String facilities;
    private String description;
    private Long creatorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 