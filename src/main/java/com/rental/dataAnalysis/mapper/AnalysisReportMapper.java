package com.rental.dataAnalysis.mapper;

import com.rental.dataAnalysis.entity.AnalysisReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AnalysisReportMapper {
    List<AnalysisReport> findAll();
    
    AnalysisReport findById(@Param("id") Long id);
    
    int insert(AnalysisReport report);
} 