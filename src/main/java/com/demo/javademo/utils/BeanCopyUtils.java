package com.demo.javademo.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    /**
     * 单个对象拷贝
     */
    public static <V, E> E copyBean(V source, Class<E> clazz) {
        if (source == null) {
            return null;
        }
        E result = null;
        try {
            result = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 集合对象拷贝
     */
    public static <V, E> List<E> copyBeanList(List<V> list, Class<E> clazz) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(item -> copyBean(item, clazz))
                .collect(Collectors.toList());
    }
} 