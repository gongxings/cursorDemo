package com.demo.javademo.service.impl;

import com.demo.javademo.dto.DashboardDTO;
import com.demo.javademo.mapper.DashboardMapper;
import com.demo.javademo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public DashboardDTO getDashboardStats() {
        DashboardDTO dto = new DashboardDTO();
        
        // 获取基础统计数据
        dto.setTotalHouses(dashboardMapper.countTotalHouses());
        dto.setTodayNewHouses(dashboardMapper.countTodayNewHouses());
        dto.setTotalUsers(dashboardMapper.countTotalUsers());
        dto.setActiveUsers(dashboardMapper.countActiveUsers());
        
        // 获取价格统计
        dto.setAvgPrice(dashboardMapper.calculateAvgPrice());
        dto.setAvgArea(dashboardMapper.calculateAvgArea());
        dto.setPricePerSquare(dashboardMapper.calculatePricePerSquare());
        
        // 获取分布数据
        dto.setDistrictDistribution(dashboardMapper.getDistrictDistribution());
        dto.setRoomTypeDistribution(dashboardMapper.getRoomTypeDistribution());
        
        // 获取价格趋势
        dto.setPriceTrend(dashboardMapper.getPriceTrend());
        
        return dto;
    }
} 