package com.demo.javademo.service.impl;

import com.demo.javademo.dto.HouseDTO;
import com.demo.javademo.dto.HouseQueryDTO;
import com.demo.javademo.entity.House;
import com.demo.javademo.exception.ErrorCode;
import com.demo.javademo.mapper.HouseMapper;
import com.demo.javademo.service.HouseService;
import com.demo.javademo.service.UserService;
import com.demo.javademo.utils.BeanCopyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<House> findAll() {
        return houseMapper.findAll();
    }

    @Override
    public House findById(Long id) {
        House house = houseMapper.findById(id);
        if (house == null) {
            throw ErrorCode.HOUSE_NOT_FOUND.exception();
        }
        return house;
    }

    @Override
    public List<House> findByDistrict(String district) {
        return houseMapper.findByDistrict(district);
    }

    @Override
    public List<House> search(HouseQueryDTO queryDTO) {
        // 使用BeanCopyUtils进行DTO到Entity的转换
        House queryEntity = BeanCopyUtils.copyBean(queryDTO, House.class);
        return houseMapper.search(queryEntity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('house:edit')")
    public House create(HouseDTO houseDTO) {
        House house = new House();
        BeanUtils.copyProperties(houseDTO, house);
        
        // 设置JSON字段
        try {
            house.setFeatures(objectMapper.writeValueAsString(houseDTO.getFeatures()));
            house.setFacilities(objectMapper.writeValueAsString(houseDTO.getFacilities()));
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        }
        
        // 设置创建者
        house.setCreatorId(userService.getCurrentUser().getId());
        house.setStatus("AVAILABLE");
        
        houseMapper.insert(house);
        return house;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('house:edit')")
    public House update(Long id, HouseDTO houseDTO) {
        House existingHouse = houseMapper.findById(id);
        if (existingHouse == null) {
            throw ErrorCode.HOUSE_NOT_FOUND.exception();
        }

        // 检查权限
        Long currentUserId = userService.getCurrentUser().getId();
        if (!currentUserId.equals(existingHouse.getCreatorId()) && 
            !userService.getCurrentUser().getRole().equals("ADMIN")) {
            throw ErrorCode.NO_PERMISSION_TO_MODIFY.exception();
        }

        BeanUtils.copyProperties(houseDTO, existingHouse);
        
        // 更新JSON字段
        try {
            existingHouse.setFeatures(objectMapper.writeValueAsString(houseDTO.getFeatures()));
            existingHouse.setFacilities(objectMapper.writeValueAsString(houseDTO.getFacilities()));
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        }

        houseMapper.update(existingHouse);
        return existingHouse;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('house:delete')")
    public void delete(Long id) {
        House house = houseMapper.findById(id);
        if (house == null) {
            throw ErrorCode.HOUSE_NOT_FOUND.exception();
        }

        // 检查权限
        Long currentUserId = userService.getCurrentUser().getId();
        if (!currentUserId.equals(house.getCreatorId()) && 
            !userService.getCurrentUser().getRole().equals("ADMIN")) {
            throw ErrorCode.NO_PERMISSION_TO_DELETE.exception();
        }

        houseMapper.deleteById(id);
    }
} 