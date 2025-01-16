package com.rental.dataAnalysis.mapper;

import com.rental.dataAnalysis.dto.HouseQueryDTO;
import com.rental.dataAnalysis.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HouseMapper {
    List<House> findAll();
    
    House findById(@Param("id") Long id);
    
    List<House> findByDistrict(@Param("district") String district);
    
    List<House> search(HouseQueryDTO queryDTO);
    
    void insert(House house);
    
    void update(House house);
    
    void deleteById(@Param("id") Long id);
} 