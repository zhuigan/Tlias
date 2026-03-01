@echo off
chcp 65001 >nul
echo ==========================================
echo   修复操作日志 500 错误
echo ==========================================
echo.

echo 📋 这个脚本将帮助你修复操作日志的 500 错误
echo.

REM 获取数据库信息
set /p DB_USER="请输入数据库用户名 (默认: root): "
if "%DB_USER%"=="" set DB_USER=root

set /p DB_PASS="请输入数据库密码: "
if "%DB_PASS%"=="" (
    echo ❌ 密码不能为空
    pause
    exit /b 1
)

set DB_NAME=tlias
echo.

echo 🔍 步骤 1: 检查数据库表...
mysql -u %DB_USER% -p%DB_PASS% -e "USE %DB_NAME%; SHOW TABLES LIKE 'operation_log';" > temp_check.txt 2>&1

findstr /C:"operation_log" temp_check.txt >nul
if %errorlevel% equ 0 (
    echo ✅ 表已存在，删除旧表...
    mysql -u %DB_USER% -p%DB_PASS% -e "USE %DB_NAME%; DROP TABLE IF EXISTS operation_log;"
)

del temp_check.txt >nul 2>&1
echo.

echo 📊 步骤 2: 创建操作日志表...
mysql -u %DB_USER% -p%DB_PASS% %DB_NAME% < database\operation_log.sql

if %errorlevel% equ 0 (
    echo ✅ 表创建成功
) else (
    echo ❌ 表创建失败
    pause
    exit /b 1
)
echo.

echo 🔍 步骤 3: 验证表结构...
mysql -u %DB_USER% -p%DB_PASS% -e "USE %DB_NAME%; DESC operation_log;"
echo.

echo 🔨 步骤 4: 重新编译项目...
call mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo ✅ 编译成功
) else (
    echo ❌ 编译失败
    pause
    exit /b 1
)
echo.

echo ==========================================
echo   ✅ 修复完成！
echo ==========================================
echo.
echo 📍 下一步:
echo   1. 重启后端服务: mvn spring-boot:run
echo   2. 刷新前端页面 (F5)
echo   3. 重新访问操作日志页面
echo.
echo 如果还有问题，请查看: fix-operation-log-error.md
echo.
pause
