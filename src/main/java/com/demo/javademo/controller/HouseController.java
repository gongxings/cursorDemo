package com.demo.javademo.controller;

import com.demo.javademo.common.Result;
import com.demo.javademo.dto.HouseDTO;
import com.demo.javademo.dto.HouseQueryDTO;
import com.demo.javademo.entity.House;
import com.demo.javademo.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Tag(name = "房源管理", description = "房源相关接口")
@RestController
@RequestMapping("/api/houses")
@Validated
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Operation(summary = "获取所有房源")
    @GetMapping
    @PreAuthorize("hasAuthority('house:view')")
    public Result<List<House>> findAll() {
        return Result.success(houseService.findAll());
    }

    @Operation(summary = "根据ID获取房源详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('house:view')")
    public Result<House> findById(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long id) {
        return Result.success(houseService.findById(id));
    }

    @Operation(summary = "根据区域查询房源")
    @GetMapping("/district/{district}")
    @PreAuthorize("hasAuthority('house:view')")
    public Result<List<House>> findByDistrict(
            @Parameter(description = "区域名称", required = true)
            @PathVariable String district) {
        return Result.success(houseService.findByDistrict(district));
    }

    @Operation(summary = "搜索房源")
    @PostMapping("/search")
    @PreAuthorize("hasAuthority('house:view')")
    public Result<List<House>> search(@RequestBody @Valid HouseQueryDTO queryDTO) {
        return Result.success(houseService.search(queryDTO));
    }

    @Operation(summary = "创建房源")
    @PostMapping
    @PreAuthorize("hasAuthority('house:edit')")
    public Result<House> create(@RequestBody @Valid HouseDTO houseDTO) {
        return Result.success("创建成功", houseService.create(houseDTO));
    }

    @Operation(summary = "更新房源")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('house:edit')")
    public Result<House> update(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid HouseDTO houseDTO) {
        return Result.success("更新成功", houseService.update(id, houseDTO));
    }

    @Operation(summary = "删除房源")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('house:delete')")
    public Result<?> delete(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long id) {
        houseService.delete(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "导入房源")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('house:edit')")
    public Result<?> importHouses(@RequestParam("file") MultipartFile file) {
        try {
            houseService.importHouses(file);
            return Result.success("导入成功");
        } catch (Exception e) {
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    @Operation(summary = "下载房源导入模板")
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("House Template");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("标题");
            header.createCell(1).setCellValue("地址");
            header.createCell(2).setCellValue("区域");
            header.createCell(3).setCellValue("价格");
            header.createCell(4).setCellValue("面积");
            header.createCell(5).setCellValue("房型");
            header.createCell(6).setCellValue("楼层");
            header.createCell(7).setCellValue("总楼层");
            header.createCell(8).setCellValue("建筑年代");
            header.createCell(9).setCellValue("房屋类型");
            header.createCell(10).setCellValue("房屋特色");
            header.createCell(11).setCellValue("配套设施");
            header.createCell(12).setCellValue("房源描述");

            // Adjust column width
            for (int i = 0; i <= 12; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "house_template.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("模板生成失败", e);
        }
    }
} 