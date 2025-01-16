package com.rental.dataAnalysis.service.impl;

import com.rental.dataAnalysis.dto.HouseDTO;
import com.rental.dataAnalysis.dto.HouseQueryDTO;
import com.rental.dataAnalysis.entity.House;
import com.rental.dataAnalysis.exception.ErrorCode;
import com.rental.dataAnalysis.mapper.HouseMapper;
import com.rental.dataAnalysis.service.HouseService;
import com.rental.dataAnalysis.service.UserService;
import com.rental.dataAnalysis.utils.BeanCopyUtils;
import com.rental.dataAnalysis.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
//        House queryEntity = BeanCopyUtils.copyBean(queryDTO, House.class);
        return houseMapper.search(queryDTO);
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
//        house.setStatus("");
        //字符串获取年份
        house.setBuildYear(String.valueOf(DateUtils.getYearFromDateStringOffset(houseDTO.getBuildYear())));
        
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

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('house:edit')")
    public void importHouses(MultipartFile file) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
                Row row = sheet.getRow(i);
                if (row == null) continue;
                House house = new House();
                house.setTitle(row.getCell(0).getStringCellValue());
                house.setAddress(row.getCell(1).getStringCellValue());
                house.setDistrict(row.getCell(2).getStringCellValue());
                house.setPrice(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
                house.setArea(BigDecimal.valueOf(row.getCell(4).getNumericCellValue()));
                house.setRoomCount((int) row.getCell(5).getNumericCellValue());
                house.setFloor((int) row.getCell(6).getNumericCellValue());
                house.setTotalFloor((int) row.getCell(7).getNumericCellValue());
                house.setBuildYear(row.getCell(8).getStringCellValue());
                house.setType(row.getCell(9).getStringCellValue());
                house.setStatus(row.getCell(10).getStringCellValue());
                house.setFeatures(objectMapper.writeValueAsString(row.getCell(11).getStringCellValue().split(",")));
                house.setFacilities(objectMapper.writeValueAsString(row.getCell(12).getStringCellValue().split(",")));
                house.setDescription(row.getCell(13).getStringCellValue());
                house.setCreatorId(userService.getCurrentUser().getId());
                houseMapper.insert(house);
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败", e);
        }
    }
} 