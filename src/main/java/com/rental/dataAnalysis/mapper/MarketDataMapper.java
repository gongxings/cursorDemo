package com.rental.dataAnalysis.mapper;

import com.rental.dataAnalysis.entity.MarketData;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MarketDataMapper {
    List<MarketData> findLastTwoMonthsData();
    
    int batchInsert(List<MarketData> dataList);
} 