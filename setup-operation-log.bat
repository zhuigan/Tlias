@echo off
chcp 65001 >nul
echo ==========================================
echo   操作日志功能 - 快速部署脚本
echo ==========================================
echo.

REM 检查 MySQL
where mysql >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 MySQL，请先安装 MySQL
    pause
    exit /b 1
)

echo ✅ MySQL 已安装
echo.

REM 提示输入数据库信息
set /p DB_HOST="数据库主机 (默认: localhost): "
if "%DB_HOST%"=="" set DB_HOST=localhost

set /p DB_PORT="数据库端口 (默认: 3306): "
if "%DB_PORT%"=="" set DB_PORT=3306

set /p DB_NAME="数据库名称 (默认: tlias): "
if "%DB_NAME%"=="" set DB_NAME=tlias

set /p DB_USER="数据库用户名 (默认: root): "
if "%DB_USER%"=="" set DB_USER=root

set /p DB_PASS="数据库密码: "
echo.

REM 执行 SQL 脚本
echo 📊 创建操作日志表...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% %DB_NAME% < database\operation_log.sql

if %errorlevel% equ 0 (
    echo ✅ 数据库表创建成功
) else (
    echo ❌ 数据库表创建失败，请检查数据库连接信息
    pause
    exit /b 1
)

echo.

REM 检查 Maven
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Maven，请先安装 Maven
    pause
    exit /b 1
)

echo ✅ Maven 已安装
echo.

REM 清理并编译
echo 🔨 清理并编译项目...
call mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo ✅ 项目编译成功
) else (
    echo ❌ 项目编译失败
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   ✅ 操作日志功能部署完成！
echo ==========================================
echo.
echo 📍 下一步操作:
echo   1. 启动后端: mvn spring-boot:run
echo   2. 启动前端: cd tlias-vue-frontend ^&^& npm run dev
echo   3. 登录管理员账号
echo   4. 访问 '操作日志' 菜单
echo.
echo 📚 详细文档: README-操作日志功能说明.md
echo.
echo ==========================================
pause
