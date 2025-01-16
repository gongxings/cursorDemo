package com.rental.dataAnalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RolePermissionMapper {
    List<String> findPermissionsByRole(@Param("role") String role);
    
    int insertRolePermission(@Param("role") String role, @Param("permission") String permission);
    
    int deleteRolePermission(@Param("role") String role, @Param("permission") String permission);
} 