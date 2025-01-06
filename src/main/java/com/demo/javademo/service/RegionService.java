package com.demo.javademo.service;

import com.demo.javademo.entity.Region;
import com.demo.javademo.mapper.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionMapper regionMapper;

    public Region findById(Long id) {
        return regionMapper.findById(id);
    }

    public List<Region> findAll() {
        return regionMapper.findAll();
    }

    public void insert(Region region) {
        regionMapper.insert(region);
    }

    public void update(Region region) {
        regionMapper.update(region);
    }

    public void delete(Long id) {
        regionMapper.delete(id);
    }
} 