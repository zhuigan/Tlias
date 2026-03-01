-- ====================================
-- Tlias 员工管理系统 - 数据库初始化脚本
-- ====================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS tlias DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tlias;

-- ====================================
-- 1. 部门表 (dept)
-- ====================================
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '部门名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 插入部门测试数据
INSERT INTO `dept` (`id`, `name`, `create_time`, `update_time`) VALUES
(1, '学工部', '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(2, '教研部', '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(3, '咨询部', '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(4, '就业部', '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(5, '人事部', '2023-09-01 00:00:00', '2023-09-01 00:00:00');

-- ====================================
-- 2. 员工表 (emp)
-- ====================================
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(32) DEFAULT '123456' COMMENT '密码',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `gender` tinyint(4) NOT NULL COMMENT '性别, 1:男, 2:女',
  `image` varchar(300) DEFAULT NULL COMMENT '图像',
  `job` tinyint(4) DEFAULT NULL COMMENT '职位, 1:班主任, 2:讲师, 3:学工主管, 4:教研主管, 5:咨询师',
  `entrydate` date DEFAULT NULL COMMENT '入职时间',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `role` tinyint(4) DEFAULT 2 COMMENT '角色, 1:管理员, 2:普通用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 插入员工测试数据
INSERT INTO `emp` (`id`, `username`, `password`, `name`, `gender`, `image`, `job`, `entrydate`, `dept_id`, `role`, `create_time`, `update_time`) VALUES
(1, 'jinyong', '123456', '金庸', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/1.jpg', 4, '2000-01-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(2, 'zhangwuji', '123456', '张无忌', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/2.jpg', 2, '2015-01-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(3, 'yangxiao', '123456', '杨逍', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/3.jpg', 2, '2008-05-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(4, 'weiyixiao', '123456', '韦一笑', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/4.jpg', 2, '2007-01-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(5, 'changyuchun', '123456', '常遇春', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/5.jpg', 2, '2012-12-05', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(6, 'xiaozhao', '123456', '小昭', 2, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/6.jpg', 3, '2013-09-05', 1, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(7, 'jixiaofu', '123456', '纪晓芙', 2, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/7.jpg', 1, '2005-08-01', 1, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(8, 'zhouzhiruo', '123456', '周芷若', 2, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/8.jpg', 1, '2014-11-09', 1, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(9, 'dingminjun', '123456', '丁敏君', 2, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/9.jpg', 1, '2011-03-11', 1, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(10, 'zhaomin', '123456', '赵敏', 2, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/10.jpg', 1, '2013-09-05', 1, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(11, 'luzhangke', '123456', '鹿杖客', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/11.jpg', 5, '2007-02-01', 3, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(12, 'hebiweng', '123456', '鹤笔翁', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/12.jpg', 5, '2008-08-18', 3, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(13, 'fangdongbai', '123456', '方东白', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/13.jpg', 5, '2012-11-01', 3, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(14, 'zhangsanfeng', '123456', '张三丰', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/14.jpg', 2, '2002-08-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(15, 'yulianzhou', '123456', '俞莲舟', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/15.jpg', 2, '2011-05-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(16, 'songyuanqiao', '123456', '宋远桥', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/16.jpg', 2, '2010-01-01', 2, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(17, 'chenyouliang', '123456', '陈友谅', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/17.jpg', NULL, '2015-03-21', NULL, 2, '2023-09-01 00:00:00', '2023-09-01 00:00:00'),
(18, 'admin', '123456', '管理员', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/web/18.jpg', 1, '2020-01-01', 1, 1, '2023-09-01 00:00:00', '2023-09-01 00:00:00');

-- ====================================
-- 数据验证查询
-- ====================================
SELECT '部门数据:' as info;
SELECT * FROM dept;

SELECT '员工数据:' as info;
SELECT * FROM emp;

SELECT '统计信息:' as info;
SELECT 
    (SELECT COUNT(*) FROM dept) as dept_count,
    (SELECT COUNT(*) FROM emp) as emp_count;
