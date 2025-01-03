package com.demo.javademo.service;

import com.demo.javademo.dto.analysis.MarketAnalysisDTO;
import com.demo.javademo.dto.analysis.PriceAnalysisDTO;
import com.demo.javademo.entity.AnalysisReport;

import java.util.List;

public interface AnalysisService {
    MarketAnalysisDTO analyzeMarket(String district);
    
    PriceAnalysisDTO analyzePricing();
    
    AnalysisReport generateReport(String type);
    
    List<AnalysisReport> getReportHistory();
    
    byte[] exportReport(Long reportId);
} 