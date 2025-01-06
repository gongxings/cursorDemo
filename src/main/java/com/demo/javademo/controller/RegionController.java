package com.demo.javademo.controller;

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
    public Region getRegionById(@PathVariable Long id) {
        return regionService.findById(id);
    }

    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.findAll();
    }

    @PostMapping
    public void createRegion(@RequestBody Region region) {
        regionService.insert(region);
    }

    @PutMapping
    public void updateRegion(@RequestBody Region region) {
        regionService.update(region);
    }

    @DeleteMapping("/{id}")
    public void deleteRegion(@PathVariable Long id) {
        regionService.delete(id);
    }
} 