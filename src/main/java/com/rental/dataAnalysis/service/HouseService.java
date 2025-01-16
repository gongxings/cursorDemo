package com.rental.dataAnalysis.service;

import com.rental.dataAnalysis.dto.HouseDTO;
import com.rental.dataAnalysis.dto.HouseQueryDTO;
import com.rental.dataAnalysis.entity.House;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface HouseService {
    List<House> findAll();
    
    House findById(Long id);
    
    List<House> findByDistrict(String district);
    
    List<House> search(HouseQueryDTO queryDTO);
    
    House create(HouseDTO houseDTO);
    
    House update(Long id, HouseDTO houseDTO);
    
    void delete(Long id);
    
    void importHouses(MultipartFile file) throws Exception;
} 