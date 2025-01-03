package com.demo.javademo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnalysisReport {
    private Long id;
    private String title;
    private String type;
    private String content;
    private Long creatorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 