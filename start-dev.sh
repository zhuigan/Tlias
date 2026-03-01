#!/bin/bash

# Tlias 员工管理系统 - 开发环境启动脚本

echo "=========================================="
echo "  Tlias 员工管理系统 - 开发环境启动"
echo "=========================================="
echo ""

# 检查 Java 环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java 环境，请先安装 JDK 11+"
    exit 1
fi

# 检查 Node.js 环境
if ! command -v node &> /dev/null; then
    echo "❌ 错误: 未找到 Node.js 环境，请先安装 Node.js 16+"
    exit 1
fi

echo "✅ Java 版本: $(java -version 2>&1 | head -n 1)"
echo "✅ Node.js 版本: $(node -v)"
echo "✅ npm 版本: $(npm -v)"
echo ""

# 启动后端
echo "🚀 正在启动后端服务..."
echo "   端口: 8080"
echo "   日志: backend.log"
mvn spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "   进程 ID: $BACKEND_PID"
echo ""

# 等待后端启动
echo "⏳ 等待后端服务启动..."
sleep 10

# 检查后端是否启动成功
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ 后端服务启动成功"
else
    echo "⚠️  后端服务可能未完全启动，请检查 backend.log"
fi
echo ""

# 启动前端
echo "🚀 正在启动前端服务..."
echo "   端口: 5173"
echo "   日志: frontend.log"
cd tlias-vue-frontend

# 检查是否已安装依赖
if [ ! -d "node_modules" ]; then
    echo "📦 首次运行，正在安装依赖..."
    npm install
fi

npm run dev > ../frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..
echo "   进程 ID: $FRONTEND_PID"
echo ""

# 保存进程 ID
echo $BACKEND_PID > .backend.pid
echo $FRONTEND_PID > .frontend.pid

echo "=========================================="
echo "  ✅ 启动完成！"
echo "=========================================="
echo ""
echo "📍 访问地址:"
echo "   前端: http://localhost:5173"
echo "   后端: http://localhost:8080"
echo ""
echo "📝 日志文件:"
echo "   后端: backend.log"
echo "   前端: frontend.log"
echo ""
echo "🛑 停止服务:"
echo "   运行: ./stop-dev.sh"
echo "   或手动: kill $BACKEND_PID $FRONTEND_PID"
echo ""
echo "=========================================="
