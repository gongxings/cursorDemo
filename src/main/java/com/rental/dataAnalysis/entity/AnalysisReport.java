package com.rental.dataAnalysis.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnalysisReport {
    private Long id;
    private String title;
    private String type;
    private String district;
    private String period;
    private String status;
    private String content;
    private Long creatorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 