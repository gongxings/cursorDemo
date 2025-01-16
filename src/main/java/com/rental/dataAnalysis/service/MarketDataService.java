package com.rental.dataAnalysis.service;

import com.rental.dataAnalysis.entity.MarketData;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MarketDataService {
    void importData(MultipartFile file);
    void batchSave(List<MarketData> dataList);
} 