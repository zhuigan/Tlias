-- ====================================
-- 添加角色字段更新脚本
-- ====================================

USE tlias;

-- 1. 添加 role 字段（如果不存在）
ALTER TABLE emp ADD COLUMN IF NOT EXISTS role TINYINT(4) DEFAULT 2 COMMENT '角色, 1:管理员, 2:普通用户';

-- 2. 更新现有数据
-- 将 admin 用户设置为管理员
UPDATE emp SET role = 1 WHERE username = 'admin';

-- 其他用户设置为普通用户
UPDATE emp SET role = 2 WHERE username != 'admin' AND (role IS NULL OR role = 0);

-- 3. 验证更新结果
SELECT 
    id, 
    username, 
    name, 
    CASE role 
        WHEN 1 THEN '管理员'
        WHEN 2 THEN '普通用户'
        ELSE '未知'
    END as role_name,
    dept_id
FROM emp
ORDER BY role, id;

-- 4. 统计信息
SELECT 
    '总用户数' as info,
    COUNT(*) as count
FROM emp
UNION ALL
SELECT 
    '管理员数量' as info,
    COUNT(*) as count
FROM emp WHERE role = 1
UNION ALL
SELECT 
    '普通用户数量' as info,
    COUNT(*) as count
FROM emp WHERE role = 2;
