CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

-- 用户表（已存在，添加角色字段）
ALTER TABLE `user` 
ADD COLUMN `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '用户角色',
ADD COLUMN `email` varchar(100) DEFAULT NULL,
ADD COLUMN `phone` varchar(20) DEFAULT NULL,
ADD COLUMN `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用';

-- 角色权限表
CREATE TABLE `role_permission` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `role` varchar(20) NOT NULL COMMENT '角色名称',
    `permission` varchar(100) NOT NULL COMMENT '权限标识',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role`,`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 房源信息表
CREATE TABLE `house` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(200) NOT NULL COMMENT '房源标题',
    `address` varchar(500) NOT NULL COMMENT '地址',
    `district` varchar(50) NOT NULL COMMENT '区域',
    `price` decimal(10,2) NOT NULL COMMENT '价格',
    `area` decimal(10,2) NOT NULL COMMENT '面积',
    `room_count` int NOT NULL COMMENT '房间数',
    `floor` int NOT NULL COMMENT '楼层',
    `total_floor` int NOT NULL COMMENT '总楼层',
    `build_year` int COMMENT '建造年份',
    `type` varchar(50) NOT NULL COMMENT '房源类型',
    `status` varchar(20) NOT NULL DEFAULT 'AVAILABLE' COMMENT '状态',
    `features` json COMMENT '特征标签',
    `facilities` json COMMENT '配套设施',
    `description` text COMMENT '详细描述',
    `creator_id` bigint NOT NULL COMMENT '创建人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_district` (`district`),
    KEY `idx_price` (`price`),
    KEY `idx_area` (`area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据分析报告表
CREATE TABLE `analysis_report` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(200) NOT NULL COMMENT '报告标题',
    `type` varchar(50) NOT NULL COMMENT '报告类型',
    `content` json NOT NULL COMMENT '报告内容',
    `creator_id` bigint NOT NULL COMMENT '创建人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 市场数据收集表
CREATE TABLE `market_data` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `source` varchar(50) NOT NULL COMMENT '数据来源',
    `district` varchar(50) NOT NULL COMMENT '区域',
    `price` decimal(10,2) NOT NULL COMMENT '价格',
    `area` decimal(10,2) NOT NULL COMMENT '面积',
    `type` varchar(50) NOT NULL COMMENT '房源类型',
    `collection_time` datetime NOT NULL COMMENT '数据采集时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_district` (`district`),
    KEY `idx_collection_time` (`collection_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 