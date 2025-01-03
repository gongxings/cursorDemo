-- 创建数据库
CREATE DATABASE IF NOT EXISTS dcursor DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE dcursor;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `role` VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色权限表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role` VARCHAR(20) NOT NULL COMMENT '角色',
    `permission` VARCHAR(50) NOT NULL COMMENT '权限',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role`, `permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

-- 房源表
CREATE TABLE IF NOT EXISTS `house` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `address` VARCHAR(200) NOT NULL COMMENT '地址',
    `district` VARCHAR(50) NOT NULL COMMENT '区域',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `area` DECIMAL(10,2) NOT NULL COMMENT '面积',
    `room_count` INT NOT NULL COMMENT '房间数',
    `floor` INT NOT NULL COMMENT '楼层',
    `total_floor` INT NOT NULL COMMENT '总楼层',
    `build_year` INT COMMENT '建筑年代',
    `type` VARCHAR(20) NOT NULL COMMENT '房屋类型',
    `status` VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '状态',
    `features` JSON COMMENT '特色',
    `facilities` JSON COMMENT '设施',
    `description` TEXT COMMENT '描述',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_district` (`district`),
    KEY `idx_creator` (`creator_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源表';

-- 分析报告表
CREATE TABLE IF NOT EXISTS `analysis_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(100) NOT NULL COMMENT '报告标题',
    `type` VARCHAR(20) NOT NULL COMMENT '报告类型',
    `content` TEXT NOT NULL COMMENT '报告内容',
    `district` VARCHAR(50) COMMENT '分析区域',
    `period` VARCHAR(20) NOT NULL COMMENT '分析周期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PROCESSING' COMMENT '状态',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_creator` (`creator_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分析报告表';

-- 市场数据表
CREATE TABLE IF NOT EXISTS `market_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `district` VARCHAR(50) NOT NULL COMMENT '区域',
    `avg_price` DECIMAL(10,2) NOT NULL COMMENT '平均价格',
    `max_price` DECIMAL(10,2) NOT NULL COMMENT '最高价格',
    `min_price` DECIMAL(10,2) NOT NULL COMMENT '最低价格',
    `total_count` INT NOT NULL COMMENT '房源总数',
    `avg_area` DECIMAL(10,2) NOT NULL COMMENT '平均面积',
    `data_date` DATE NOT NULL COMMENT '数据日期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_district_date` (`district`, `data_date`),
    KEY `idx_date` (`data_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='市场数据表';

-- 初始化角色权限数据
INSERT INTO `role_permission` (`role`, `permission`) VALUES
('ADMIN', 'user:view'),
('ADMIN', 'user:edit'),
('ADMIN', 'user:delete'),
('ADMIN', 'house:view'),
('ADMIN', 'house:edit'),
('ADMIN', 'house:delete'),
('ADMIN', 'analysis:view'),
('ADMIN', 'analysis:generate'),
('USER', 'house:view'),
('USER', 'house:edit'),
('USER', 'analysis:view');

-- 初始化管理员账号 (密码: password)
INSERT INTO `user` (`username`, `password`, `role`, `email`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt2s7LC', 'ADMIN', 'admin@example.com', 1); 