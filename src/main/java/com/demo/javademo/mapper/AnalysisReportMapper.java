package com.demo.javademo.mapper;

import com.demo.javademo.entity.AnalysisReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AnalysisReportMapper {
    List<AnalysisReport> findAll();
    
    AnalysisReport findById(@Param("id") Long id);
    
    int insert(AnalysisReport report);
} 