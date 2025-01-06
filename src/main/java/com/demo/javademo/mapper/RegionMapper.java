package com.demo.javademo.mapper;

import com.demo.javademo.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RegionMapper {

    @Select("SELECT * FROM region WHERE id = #{id}")
    Region findById(Long id);

    @Select("SELECT * FROM region")
    List<Region> findAll();

    @Insert("INSERT INTO region(province, city, county) VALUES(#{province}, #{city}, #{county})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Region region);

    @Update("UPDATE region SET province=#{province}, city=#{city}, county=#{county} WHERE id=#{id}")
    void update(Region region);

    @Delete("DELETE FROM region WHERE id=#{id}")
    void delete(Long id);
} 