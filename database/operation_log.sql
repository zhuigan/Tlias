-- 创建操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) COMMENT '操作用户',
    operation VARCHAR(50) COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    ip VARCHAR(50) COMMENT 'IP地址',
    operate_time DATETIME COMMENT '操作时间',
    result VARCHAR(20) COMMENT '操作结果'
) COMMENT '操作日志表';

-- 创建索引
CREATE INDEX idx_username ON operation_log(username);
CREATE INDEX idx_operate_time ON operation_log(operate_time);
