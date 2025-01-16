package com.rental.dataAnalysis.service;

import com.rental.dataAnalysis.dto.analysis.MarketAnalysisDTO;
import com.rental.dataAnalysis.dto.analysis.PriceAnalysisDTO;
import com.rental.dataAnalysis.entity.AnalysisReport;

import java.util.List;

public interface AnalysisService {
    MarketAnalysisDTO analyzeMarket(String district);
    
    PriceAnalysisDTO analyzePricing();
    
    AnalysisReport generateReport(String type);
    
    List<AnalysisReport> getReportHistory();
    
    byte[] exportReport(Long reportId);
} 