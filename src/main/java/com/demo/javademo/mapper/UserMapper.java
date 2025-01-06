package com.demo.javademo.mapper;

import com.demo.javademo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    
    User findById(@Param("id") Long id);
    
    int insert(User user);
    
    int update(User user);
    
    int delete(@Param("id") Long id);
    
    List<User> findAll();
    
    int updatePassword(@Param("id") Long id, @Param("password") String password);
} 