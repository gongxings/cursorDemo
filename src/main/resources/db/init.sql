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


INSERT INTO house (title, address, district, price, area, room_count, floor, total_floor, build_year, type, status, features, facilities, description, creator_id, create_time, update_time)
VALUES
    ('豪华公寓', '北京市朝阳区望京街道', '朝阳区', 12000000, 120, 3, 5, 20, '2015', '公寓', '在售', '["阳光充足", "交通便利"]', '["电梯", "停车位"]', '位于市中心，交通便利，周边设施齐全。', 1, NOW(), NOW()),
    ('现代别墅', '上海市浦东新区陆家嘴', '浦东新区', 30000000, 300, 2, 2, 10, '2010', '别墅', '在售', '["花园", "游泳池"]', '["车库", "健身房"]', '豪华别墅，配备私人花园和游泳池。', 2, NOW(), NOW()),
    ('经济型住宅', '广州市天河区天河北路', '天河区', 800000, 80, 8, 20, 15, '2005', '住宅', '已售', '["采光好"]', '["公共停车场"]', '经济实惠，适合刚需购房者。', 3, NOW(), NOW()),
    ('海景房', '深圳市南山区蛇口', '南山区', 15000000, 150, 10, 30, 5, '2018', '公寓', '在售', '["海景", "高层"]', '["游泳池", "健身房"]', '高层海景房，视野开阔，设施齐全。', 4, NOW(), NOW()),
    ('学区房', '杭州市西湖区文三路', '西湖区', 2000000, 100, 5, 10, 8, '2012', '住宅', '在售', '["学区房"]', '["停车位"]', '靠近名校，适合家庭居住。', 5, NOW(), NOW());


CREATE TABLE UserToken (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           userId BIGINT NOT NULL,
                           token VARCHAR(255) NOT NULL,
                           createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (userId) REFERENCES User(id)
);


CREATE TABLE region (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    province VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    county VARCHAR(255) NOT NULL
);

INSERT INTO region (province, city, county) VALUES
('北京市', '北京市', '东城区'),
('北京市', '北京市', '西城区'),
('上海市', '上海市', '黄浦区'),
('上海市', '上海市', '徐汇区'),
('广东省', '广州市', '天河区'),
('广东省', '深圳市', '南山区');