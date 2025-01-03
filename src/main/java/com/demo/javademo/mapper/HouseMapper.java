package com.demo.javademo.mapper;

import com.demo.javademo.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HouseMapper {
    List<House> findAll();
    
    House findById(@Param("id") Long id);
    
    List<House> findByDistrict(@Param("district") String district);
    
    List<House> search(House query);
    
    void insert(House house);
    
    void update(House house);
    
    void deleteById(@Param("id") Long id);
} 