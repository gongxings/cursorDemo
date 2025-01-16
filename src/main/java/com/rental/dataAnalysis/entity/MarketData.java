package com.rental.dataAnalysis.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MarketData {
    private Long id;
    private String source;
    private String district;
    private BigDecimal price;
    private BigDecimal area;
    private String type;
    private LocalDateTime collectionTime;
    private LocalDateTime createTime;
} 