package com.demo.javademo.service;

import com.demo.javademo.entity.MarketData;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MarketDataService {
    void importData(MultipartFile file);
    void batchSave(List<MarketData> dataList);
} 