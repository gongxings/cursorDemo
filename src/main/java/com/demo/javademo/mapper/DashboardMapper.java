package com.demo.javademo.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {
    Long countTotalHouses();
    Long countTodayNewHouses();
    Long countTotalUsers();
    Long countActiveUsers();
    BigDecimal calculateAvgPrice();
    BigDecimal calculateAvgArea();
    BigDecimal calculatePricePerSquare();
    List<Map<String, Object>> getDistrictDistribution();
    List<Map<String, Object>> getRoomTypeDistribution();
    List<Map<String, Object>> getPriceTrend();
} 