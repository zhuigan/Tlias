#!/bin/bash

echo "=========================================="
echo "  操作日志功能 - 快速部署脚本"
echo "=========================================="
echo ""

# 检查 MySQL 是否运行
echo "🔍 检查 MySQL 服务..."
if ! command -v mysql &> /dev/null; then
    echo "❌ 错误: 未找到 MySQL，请先安装 MySQL"
    exit 1
fi

echo "✅ MySQL 已安装"
echo ""

# 提示输入数据库信息
echo "📝 请输入数据库信息:"
read -p "数据库主机 (默认: localhost): " DB_HOST
DB_HOST=${DB_HOST:-localhost}

read -p "数据库端口 (默认: 3306): " DB_PORT
DB_PORT=${DB_PORT:-3306}

read -p "数据库名称 (默认: tlias): " DB_NAME
DB_NAME=${DB_NAME:-tlias}

read -p "数据库用户名 (默认: root): " DB_USER
DB_USER=${DB_USER:-root}

read -sp "数据库密码: " DB_PASS
echo ""
echo ""

# 执行 SQL 脚本
echo "📊 创建操作日志表..."
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASS $DB_NAME < database/operation_log.sql

if [ $? -eq 0 ]; then
    echo "✅ 数据库表创建成功"
else
    echo "❌ 数据库表创建失败，请检查数据库连接信息"
    exit 1
fi

echo ""

# 检查 Maven
echo "🔍 检查 Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 未找到 Maven，请先安装 Maven"
    exit 1
fi

echo "✅ Maven 已安装"
echo ""

# 清理并编译
echo "🔨 清理并编译项目..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ 项目编译成功"
else
    echo "❌ 项目编译失败"
    exit 1
fi

echo ""
echo "=========================================="
echo "  ✅ 操作日志功能部署完成！"
echo "=========================================="
echo ""
echo "📍 下一步操作:"
echo "  1. 启动后端: mvn spring-boot:run"
echo "  2. 启动前端: cd tlias-vue-frontend && npm run dev"
echo "  3. 登录管理员账号"
echo "  4. 访问 '操作日志' 菜单"
echo ""
echo "📚 详细文档: README-操作日志功能说明.md"
echo ""
echo "=========================================="
