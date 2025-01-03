package com.demo.javademo.service.impl;

import com.demo.javademo.dto.analysis.MarketAnalysisDTO;
import com.demo.javademo.dto.analysis.PriceAnalysisDTO;
import com.demo.javademo.entity.AnalysisReport;
import com.demo.javademo.entity.House;
import com.demo.javademo.entity.MarketData;
import com.demo.javademo.exception.ErrorCode;
import com.demo.javademo.mapper.AnalysisReportMapper;
import com.demo.javademo.mapper.HouseMapper;
import com.demo.javademo.mapper.MarketDataMapper;
import com.demo.javademo.service.AnalysisService;
import com.demo.javademo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private MarketDataMapper marketDataMapper;

    @Autowired
    private AnalysisReportMapper analysisReportMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MarketAnalysisDTO analyzeMarket(String district) {
        List<House> houses = houseMapper.findByDistrict(district);
        
        MarketAnalysisDTO analysis = new MarketAnalysisDTO();
        analysis.setDistrict(district);
        analysis.setTotalCount(houses.size());
        
        if (!houses.isEmpty()) {
            DoubleSummaryStatistics priceStats = houses.stream()
                    .mapToDouble(h -> h.getPrice().doubleValue())
                    .summaryStatistics();
            
            DoubleSummaryStatistics areaStats = houses.stream()
                    .mapToDouble(h -> h.getArea().doubleValue())
                    .summaryStatistics();
            
            analysis.setAvgPrice(BigDecimal.valueOf(priceStats.getAverage()).setScale(2, RoundingMode.HALF_UP));
            analysis.setMaxPrice(BigDecimal.valueOf(priceStats.getMax()).setScale(2, RoundingMode.HALF_UP));
            analysis.setMinPrice(BigDecimal.valueOf(priceStats.getMin()).setScale(2, RoundingMode.HALF_UP));
            analysis.setAvgArea(BigDecimal.valueOf(areaStats.getAverage()).setScale(2, RoundingMode.HALF_UP));
            
            BigDecimal totalPricePerMeter = houses.stream()
                    .map(h -> h.getPrice().divide(h.getArea(), 2, RoundingMode.HALF_UP))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            analysis.setPricePerSquareMeter(totalPricePerMeter.divide(BigDecimal.valueOf(houses.size()), 2, RoundingMode.HALF_UP));
        }
        
        return analysis;
    }

    @Override
    public PriceAnalysisDTO analyzePricing() {
        List<House> houses = houseMapper.findAll();
        PriceAnalysisDTO analysis = new PriceAnalysisDTO();
        
        // 计算各区域平均价格
        Map<String, BigDecimal> districtAvgPrices = houses.stream()
                .collect(Collectors.groupingBy(
                        House::getDistrict,
                        Collectors.averagingDouble(h -> h.getPrice().doubleValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP)));
        
        // 计算各类型平均价格
        Map<String, BigDecimal> typeAvgPrices = houses.stream()
                .collect(Collectors.groupingBy(
                        House::getType,
                        Collectors.averagingDouble(h -> h.getPrice().doubleValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP)));
        
        // 计算各房间数量平均价格
        Map<Integer, BigDecimal> roomCountAvgPrices = houses.stream()
                .collect(Collectors.groupingBy(
                        House::getRoomCount,
                        Collectors.averagingDouble(h -> h.getPrice().doubleValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP)));
        
        analysis.setDistrictAvgPrices(districtAvgPrices);
        analysis.setTypeAvgPrices(typeAvgPrices);
        analysis.setRoomCountAvgPrices(roomCountAvgPrices);
        
        // 计算总体平均价格
        if (!houses.isEmpty()) {
            double avgPrice = houses.stream()
                    .mapToDouble(h -> h.getPrice().doubleValue())
                    .average()
                    .orElse(0.0);
            analysis.setOverallAvgPrice(BigDecimal.valueOf(avgPrice).setScale(2, RoundingMode.HALF_UP));
        }
        
        // 计算价格增长率（与上月相比）
        List<MarketData> marketData = marketDataMapper.findLastTwoMonthsData();
        if (marketData.size() >= 2) {
            BigDecimal lastMonthAvg = marketData.get(0).getPrice();
            BigDecimal previousMonthAvg = marketData.get(1).getPrice();
            BigDecimal growthRate = lastMonthAvg.subtract(previousMonthAvg)
                    .divide(previousMonthAvg, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            analysis.setPriceGrowthRate(growthRate);
        }
        
        return analysis;
    }

    @Override
    @Transactional
    public AnalysisReport generateReport(String type) {
        AnalysisReport report = new AnalysisReport();
        report.setTitle(type + "分析报告-" + new Date());
        report.setType(type);
        report.setCreatorId(userService.getCurrentUser().getId());
        
        Map<String, Object> content = new HashMap<>();
        if ("market".equals(type)) {
            content.put("marketAnalysis", analyzeMarket(null));
        } else if ("price".equals(type)) {
            content.put("priceAnalysis", analyzePricing());
        }
        
        try {
            report.setContent(objectMapper.writeValueAsString(content));
        } catch (Exception e) {
            throw ErrorCode.SYSTEM_ERROR.exception("生成报告失败");
        }
        
        analysisReportMapper.insert(report);
        return report;
    }

    @Override
    public List<AnalysisReport> getReportHistory() {
        return analysisReportMapper.findAll();
    }

    @Override
    public byte[] exportReport(Long reportId) {
        AnalysisReport report = analysisReportMapper.findById(reportId);
        if (report == null) {
            throw ErrorCode.PARAM_ERROR.exception("报告不存在");
        }
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("分析报告");
            
            // 创建标题行
            XSSFRow titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("报告标题");
            titleRow.createCell(1).setCellValue(report.getTitle());
            
            // 创建内容
            Map<String, Object> content = objectMapper.readValue(report.getContent(), Map.class);
            int rowNum = 2;
            
            for (Map.Entry<String, Object> entry : content.entrySet()) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue().toString());
            }
            
            // 导出为字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw ErrorCode.SYSTEM_ERROR.exception("导出报告失败");
        }
    }
} 