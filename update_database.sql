USE tlias;

-- 添加 role 字段
ALTER TABLE emp ADD COLUMN role TINYINT(4) DEFAULT 2 COMMENT '角色, 1:管理员, 2:普通用户';

-- 设置管理员
UPDATE emp SET role = 1 WHERE username = 'admin';

-- 设置普通用户
UPDATE emp SET role = 2 WHERE username != 'admin';

-- 验证结果
SELECT id, username, name, role, dept_id FROM emp;
