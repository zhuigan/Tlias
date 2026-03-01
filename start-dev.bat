@echo off
chcp 65001 >nul
echo ==========================================
echo   Tlias 员工管理系统 - 开发环境启动
echo ==========================================
echo.

REM 检查 Java 环境
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Java 环境，请先安装 JDK 11+
    pause
    exit /b 1
)

REM 检查 Node.js 环境
where node >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Node.js 环境，请先安装 Node.js 16+
    pause
    exit /b 1
)

echo ✅ 环境检查通过
echo.

REM 启动后端
echo 🚀 正在启动后端服务...
echo    端口: 8080
start "Tlias Backend" cmd /c "mvn spring-boot:run"
timeout /t 10 /nobreak >nul
echo ✅ 后端服务已启动
echo.

REM 启动前端
echo 🚀 正在启动前端服务...
echo    端口: 5173
cd tlias-vue-frontend

REM 检查是否已安装依赖
if not exist "node_modules" (
    echo 📦 首次运行，正在安装依赖...
    call npm install
)

start "Tlias Frontend" cmd /c "npm run dev"
cd ..
echo ✅ 前端服务已启动
echo.

echo ==========================================
echo   ✅ 启动完成！
echo ==========================================
echo.
echo 📍 访问地址:
echo    前端: http://localhost:5173
echo    后端: http://localhost:8080
echo.
echo 🛑 停止服务: 关闭对应的命令行窗口
echo.
echo ==========================================
pause
