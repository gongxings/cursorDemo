package com.demo.javademo.controller;

import com.demo.javademo.common.Result;
import com.demo.javademo.entity.Region;
import com.demo.javademo.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/{id}")
    public Result<Region> getRegionById(@PathVariable Long id) {
        return Result.success(regionService.findById(id));
    }

    @GetMapping
    public Result<List<Region>> getAllRegions() {
        return Result.success(regionService.findAll());
    }

    @PostMapping
    public void createRegion(@RequestBody Region region) {
        regionService.insert(region);
        Result.success("操作成功",region);
    }

    @PutMapping
    public void updateRegion(@RequestBody Region region) {
        regionService.update(region);
        Result.success("更新成功",region);
    }

    @DeleteMapping("/{id}")
    public void deleteRegion(@PathVariable Long id) {
        regionService.delete(id);
        Result.success("删除成功");
    }
} 