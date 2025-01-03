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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
} 