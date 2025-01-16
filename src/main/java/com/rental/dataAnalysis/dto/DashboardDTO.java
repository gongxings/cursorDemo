package com.rental.dataAnalysis.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDTO {
    // 统计数据
    private Long totalHouses;          // 房源总数
    private Long todayNewHouses;       // 今日新增房源
    private Long totalUsers;           // 用户总数
    private Long activeUsers;          // 活跃用户数
    
    // 价格统计
    private BigDecimal avgPrice;       // 平均价格
    private BigDecimal avgArea;        // 平均面积
    private BigDecimal pricePerSquare; // 均价（元/平米）
    
    // 区域分布
    private List<Map<String, Object>> districtDistribution;  // 区域分布数据
    
    // 房型分布
    private List<Map<String, Object>> roomTypeDistribution;  // 房型分布数据
    
    // 价格趋势（近7天）
    private List<Map<String, Object>> priceTrend;           // 价格趋势数据
} 