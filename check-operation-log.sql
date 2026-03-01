-- 检查操作日志表是否存在
USE tlias;

-- 查看所有表
SHOW TABLES;

-- 检查 operation_log 表
SHOW TABLES LIKE 'operation_log';

-- 如果表存在，查看表结构
DESC operation_log;

-- 查看表中的数据
SELECT * FROM operation_log ORDER BY operate_time DESC LIMIT 10;
