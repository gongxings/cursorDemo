package com.demo.javademo.controller;

import com.demo.javademo.common.Result;
import com.demo.javademo.entity.MarketData;
import com.demo.javademo.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "市场数据", description = "市场数据采集相关接口")
@RestController
@RequestMapping("/api/market-data")
public class MarketDataController {

    @Autowired
    private MarketDataService marketDataService;

    @Operation(summary = "导入市场数据")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('market:import')")
    public Result<?> importData(@RequestParam("file") MultipartFile file) {
        marketDataService.importData(file);
        return Result.success("数据导入成功");
    }

    @Operation(summary = "批量保存市场数据")
    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('market:import')")
    public Result<?> batchSave(@RequestBody List<MarketData> dataList) {
        marketDataService.batchSave(dataList);
        return Result.success("数据保存成功");
    }
} 