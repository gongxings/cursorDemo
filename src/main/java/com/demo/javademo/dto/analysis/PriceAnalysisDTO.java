package com.demo.javademo.dto.analysis;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class PriceAnalysisDTO {
    private Map<String, BigDecimal> districtAvgPrices;
    private Map<String, BigDecimal> typeAvgPrices;
    private Map<Integer, BigDecimal> roomCountAvgPrices;
    private BigDecimal overallAvgPrice;
    private BigDecimal priceGrowthRate;
} 