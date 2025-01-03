package com.demo.javademo.service.impl;

import com.demo.javademo.entity.MarketData;
import com.demo.javademo.exception.ErrorCode;
import com.demo.javademo.mapper.MarketDataMapper;
import com.demo.javademo.service.MarketDataService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    @Autowired
    private MarketDataMapper marketDataMapper;

    @Override
    @Transactional
    public void importData(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            
            List<MarketData> dataList = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 跳过表头
                
                MarketData data = new MarketData();
                data.setSource(row.getCell(0).getStringCellValue());
                data.setDistrict(row.getCell(1).getStringCellValue());
                data.setPrice(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));
                data.setArea(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
                data.setType(row.getCell(4).getStringCellValue());
                data.setCollectionTime(LocalDateTime.now());
                
                dataList.add(data);
            }
            
            if (!dataList.isEmpty()) {
                marketDataMapper.batchInsert(dataList);
            }
            
        } catch (Exception e) {
            throw ErrorCode.SYSTEM_ERROR.exception("数据导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void batchSave(List<MarketData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw ErrorCode.PARAM_ERROR.exception("数据列表不能为空");
        }
        
        dataList.forEach(data -> data.setCollectionTime(LocalDateTime.now()));
        marketDataMapper.batchInsert(dataList);
    }
} 