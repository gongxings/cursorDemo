package com.demo.javademo.controller;

import com.demo.javademo.common.Result;
import com.demo.javademo.dto.DashboardDTO;
import com.demo.javademo.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "首页", description = "首页数据展示相关接口")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "获取首页数据")
    @GetMapping("/stats")
    public Result<DashboardDTO> getDashboardStats() {
        return Result.success(dashboardService.getDashboardStats());
    }
} 