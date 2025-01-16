package com.rental.dataAnalysis.dto.analysis;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MarketAnalysisDTO {
    private String district;
    private BigDecimal avgPrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Integer totalCount;
    private BigDecimal avgArea;
    private BigDecimal pricePerSquareMeter;
} 