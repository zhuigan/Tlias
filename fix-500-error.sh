#!/bin/bash

echo "=========================================="
echo "  修复操作日志 500 错误"
echo "=========================================="
echo ""

echo "📋 这个脚本将帮助你修复操作日志的 500 错误"
echo ""

# 获取数据库信息
read -p "请输入数据库用户名 (默认: root): " DB_USER
DB_USER=${DB_USER:-root}

read -sp "请输入数据库密码: " DB_PASS
echo ""

if [ -z "$DB_PASS" ]; then
    echo "❌ 密码不能为空"
    exit 1
fi

DB_NAME=tlias
echo ""

echo "🔍 步骤 1: 检查数据库表..."
TABLE_EXISTS=$(mysql -u $DB_USER -p$DB_PASS -e "USE $DB_NAME; SHOW TABLES LIKE 'operation_log';" 2>/dev/null | grep operation_log)

if [ ! -z "$TABLE_EXISTS" ]; then
    echo "✅ 表已存在，删除旧表..."
    mysql -u $DB_USER -p$DB_PASS -e "USE $DB_NAME; DROP TABLE IF EXISTS operation_log;"
fi
echo ""

echo "📊 步骤 2: 创建操作日志表..."
mysql -u $DB_USER -p$DB_PASS $DB_NAME < database/operation_log.sql

if [ $? -eq 0 ]; then
    echo "✅ 表创建成功"
else
    echo "❌ 表创建失败"
    exit 1
fi
echo ""

echo "🔍 步骤 3: 验证表结构..."
mysql -u $DB_USER -p$DB_PASS -e "USE $DB_NAME; DESC operation_log;"
echo ""

echo "🔨 步骤 4: 重新编译项目..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ 编译成功"
else
    echo "❌ 编译失败"
    exit 1
fi
echo ""

echo "=========================================="
echo "  ✅ 修复完成！"
echo "=========================================="
echo ""
echo "📍 下一步:"
echo "  1. 重启后端服务: mvn spring-boot:run"
echo "  2. 刷新前端页面 (F5)"
echo "  3. 重新访问操作日志页面"
echo ""
echo "如果还有问题，请查看: fix-operation-log-error.md"
echo ""
