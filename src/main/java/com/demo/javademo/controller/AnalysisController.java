package com.demo.javademo.controller;

import com.demo.javademo.common.Result;
import com.demo.javademo.dto.analysis.MarketAnalysisDTO;
import com.demo.javademo.dto.analysis.PriceAnalysisDTO;
import com.demo.javademo.entity.AnalysisReport;
import com.demo.javademo.service.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据分析", description = "数据分析相关接口")
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Operation(summary = "市场分析")
    @GetMapping("/market/{district}")
    @PreAuthorize("hasAuthority('analysis:view')")
    public Result<MarketAnalysisDTO> analyzeMarket(
            @Parameter(description = "区域名称", required = true)
            @PathVariable String district) {
        return Result.success(analysisService.analyzeMarket(district));
    }

    @Operation(summary = "价格分析")
    @GetMapping("/price")
    @PreAuthorize("hasAuthority('analysis:view')")
    public Result<PriceAnalysisDTO> analyzePricing() {
        return Result.success(analysisService.analyzePricing());
    }

    @Operation(summary = "生成分析报告")
    @PostMapping("/report/{type}")
    @PreAuthorize("hasAuthority('analysis:generate')")
    public Result<AnalysisReport> generateReport(
            @Parameter(description = "报告类型(market/price)", required = true)
            @PathVariable String type) {
        return Result.success(analysisService.generateReport(type));
    }

    @Operation(summary = "获取报告历史")
    @GetMapping("/reports")
    @PreAuthorize("hasAuthority('analysis:view')")
    public Result<List<AnalysisReport>> getReportHistory() {
        return Result.success(analysisService.getReportHistory());
    }

    @Operation(summary = "导出报告")
    @GetMapping("/report/export/{reportId}")
    @PreAuthorize("hasAuthority('analysis:view')")
    public ResponseEntity<byte[]> exportReport(
            @Parameter(description = "报告ID", required = true)
            @PathVariable Long reportId) {
        byte[] reportBytes = analysisService.exportReport(reportId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
} 