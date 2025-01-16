package com.rental.dataAnalysis.controller;

import com.rental.dataAnalysis.common.Result;
import com.rental.dataAnalysis.dto.HouseDTO;
import com.rental.dataAnalysis.dto.HouseQueryDTO;
import com.rental.dataAnalysis.entity.House;
import com.rental.dataAnalysis.service.HouseService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            Sheet sheet = workbook.createSheet("房源信息");
            Row header = sheet.createRow(0);
            // 设置列宽(单位:字符宽度)
            // 定义表头和列宽信息
            List<Map<String, Object>> columns = Arrays.asList(
                Map.of("title", "标题", "width", 200),
                Map.of("title", "地址", "width", 300),
                Map.of("title", "区域", "width", 150),
                Map.of("title", "价格", "width", 150),
                Map.of("title", "面积", "width", 150),
                Map.of("title", "房型", "width", 100),
                Map.of("title", "楼层", "width", 100),
                Map.of("title", "总楼层", "width", 100),
                Map.of("title", "建筑年代", "width", 150),
                Map.of("title", "房屋类型", "width", 150),
                    Map.of("title", "房屋状态", "width", 150),
                Map.of("title", "房屋特色", "width", 300),
                Map.of("title", "配套设施", "width", 300),
                Map.of("title", "房源描述", "width", 400)
            );

            // 设置列宽和表头
            for (int i = 0; i < columns.size(); i++) {
                Map<String, Object> column = columns.get(i);
                // 设置列宽,并使用自适应
                sheet.setColumnWidth(i, (int)column.get("width"));
//                sheet.autoSizeColumn(i);
                // 创建表头
                header.createCell(i).setCellValue((String)column.get("title"));
            }

            // Adjust column width
            for (int i = 0; i <= 12; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "template.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("模板生成失败", e);
        }
    }
} 