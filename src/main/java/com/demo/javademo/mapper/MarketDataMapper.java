package com.demo.javademo.mapper;

import com.demo.javademo.entity.MarketData;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MarketDataMapper {
    List<MarketData> findLastTwoMonthsData();
    
    int batchInsert(List<MarketData> dataList);
} 