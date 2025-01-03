package com.demo.javademo.service;

import com.demo.javademo.dto.HouseDTO;
import com.demo.javademo.dto.HouseQueryDTO;
import com.demo.javademo.entity.House;
import java.util.List;

public interface HouseService {
    List<House> findAll();
    
    House findById(Long id);
    
    List<House> findByDistrict(String district);
    
    List<House> search(HouseQueryDTO queryDTO);
    
    House create(HouseDTO houseDTO);
    
    House update(Long id, HouseDTO houseDTO);
    
    void delete(Long id);
} 