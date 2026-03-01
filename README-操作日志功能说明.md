# 操作日志功能说明

## 📝 功能概述

系统已集成完整的操作日志功能，可以实时记录和展示用户的各种操作，包括：

- ✅ 用户登录
- ✅ 用户注册
- ✅ 上传文件/头像
- ✅ 添加员工
- ✅ 修改员工信息
- ✅ 删除员工
- ✅ 更新头像

## 🏗️ 技术实现

### 后端实现

#### 1. 数据库表结构

```sql
CREATE TABLE operation_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),      -- 操作用户
    operation VARCHAR(50),     -- 操作类型
    method VARCHAR(200),       -- 请求方法
    params TEXT,               -- 请求参数
    ip VARCHAR(50),            -- IP地址
    operate_time DATETIME,     -- 操作时间
    result VARCHAR(20)         -- 操作结果
);
```

#### 2. AOP 切面记录

使用 Spring AOP 自动拦截带有 `@OperationLogger` 注解的方法，记录操作日志。

**核心文件**：
- `src/main/java/com/itheima/aspect/OperationLogAspect.java` - AOP 切面
- `src/main/java/com/itheima/annotation/OperationLogger.java` - 自定义注解
- `src/main/java/com/itheima/pojo/OperationLog.java` - 实体类
- `src/main/java/com/itheima/mapper/OperationLogMapper.java` - Mapper
- `src/main/java/com/itheima/controller/OperationLogController.java` - 控制器

#### 3. 使用方式

在需要记录日志的方法上添加注解：

```java
@OperationLogger("用户登录")
@PostMapping("/login")
public Result login(@RequestBody Emp emp) {
    // 方法实现
}
```

### 前端实现

#### 1. 操作日志页面

**文件位置**：`tlias-vue-frontend/src/views/OperationLog.vue`

**功能特性**：
- 📊 实时统计（总操作数、今日操作、成功/失败数）
- 🔄 自动刷新（每5秒刷新一次）
- 📋 详细日志列表
- 🎨 彩色标签区分操作类型
- 📱 响应式设计

#### 2. API 接口

**文件位置**：`tlias-vue-frontend/src/api/log.js`

```javascript
// 获取最近的操作日志
getOperationLogs(limit)

// 获取所有操作日志
getAllLogs()
```

## 🚀 快速开始

### 1. 创建数据库表

在 MySQL 中执行：

```bash
mysql -u root -p tlias < database/operation_log.sql
```

或手动执行 `database/operation_log.sql` 中的 SQL 语句。

### 2. 重启后端服务

```bash
# 停止当前服务
Ctrl+C

# 清理并重新编译
mvn clean install

# 启动服务
mvn spring-boot:run
```

### 3. 访问操作日志页面

1. 启动前端：`cd tlias-vue-frontend && npm run dev`
2. 登录系统（使用管理员账号）
3. 点击左侧菜单 "操作日志"

## 📊 功能展示

### 统计信息

页面顶部显示四个统计卡片：
- 总操作数
- 今日操作
- 成功操作
- 失败操作

### 日志列表

表格显示详细的操作记录：
- 操作时间
- 操作用户
- 操作类型（带颜色标签）
- IP地址
- 请求参数（长参数可悬停查看）
- 操作结果（成功/失败）

### 自动刷新

- 默认开启自动刷新（每5秒）
- 可通过开关控制
- 手动刷新按钮

## 🎨 操作类型颜色

| 操作类型 | 颜色 |
|---------|------|
| 用户登录 | 蓝色 (primary) |
| 用户注册 | 绿色 (success) |
| 上传文件 | 橙色 (warning) |
| 更新头像 | 灰色 (info) |
| 添加员工 | 绿色 (success) |
| 修改员工信息 | 橙色 (warning) |
| 删除员工 | 红色 (danger) |

## 🔧 自定义配置

### 添加新的操作日志

1. 在控制器方法上添加注解：

```java
@OperationLogger("你的操作名称")
@PostMapping("/your-endpoint")
public Result yourMethod(@RequestBody YourData data) {
    // 方法实现
}
```

2. 重启后端服务即可

### 修改自动刷新间隔

编辑 `tlias-vue-frontend/src/views/OperationLog.vue`：

```javascript
const startAutoRefresh = () => {
  refreshTimer = setInterval(() => {
    loadLogs()
  }, 5000) // 修改这里的毫秒数，5000 = 5秒
}
```

### 修改日志保留数量

编辑 `tlias-vue-frontend/src/views/OperationLog.vue`：

```javascript
const loadLogs = async () => {
  const res = await getOperationLogs(100) // 修改这里的数字
  // ...
}
```

## 📝 日志记录内容

### 自动记录

- ✅ 操作用户（从 JWT Token 解析）
- ✅ 操作类型（注解中定义）
- ✅ 请求方法（类名.方法名）
- ✅ 请求参数（自动隐藏密码）
- ✅ IP地址（支持代理）
- ✅ 操作时间
- ✅ 操作结果（成功/失败）

### 密码保护

系统会自动将请求参数中的密码替换为 `******`，保护用户隐私。

## 🔐 权限控制

- 只有管理员可以查看操作日志
- 普通用户无法访问日志页面
- 路由守卫自动拦截未授权访问

## 📱 响应式设计

操作日志页面支持：
- 桌面端（大屏幕）
- 平板端（中等屏幕）
- 移动端（小屏幕）

## 🐛 故障排查

### 问题1：日志表不存在

**错误信息**：`Table 'tlias.operation_log' doesn't exist`

**解决方案**：
```bash
mysql -u root -p tlias < database/operation_log.sql
```

### 问题2：日志没有记录

**检查步骤**：
1. 确认方法上有 `@OperationLogger` 注解
2. 确认 AOP 依赖已添加到 pom.xml
3. 查看后端控制台是否有错误
4. 检查数据库连接是否正常

### 问题3：前端无法加载日志

**检查步骤**：
1. 确认已登录且是管理员账号
2. 打开浏览器控制台查看网络请求
3. 确认后端 `/logs/recent` 接口正常
4. 检查 Token 是否有效

## 📈 性能优化

### 数据库索引

已创建索引优化查询性能：
```sql
CREATE INDEX idx_username ON operation_log(username);
CREATE INDEX idx_operate_time ON operation_log(operate_time);
```

### 分页查询

默认查询最近 100 条记录，避免一次性加载过多数据。

### 定期清理

建议定期清理旧日志：
```sql
-- 删除30天前的日志
DELETE FROM operation_log 
WHERE operate_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

## 🎯 最佳实践

1. **合理使用注解**：只在关键操作上添加日志注解
2. **定期清理**：避免日志表过大影响性能
3. **监控异常**：关注失败的操作，及时发现问题
4. **保护隐私**：确保敏感信息不被记录
5. **备份日志**：定期备份重要的操作日志

## 📚 扩展功能

可以考虑添加的功能：
- [ ] 日志导出（Excel/CSV）
- [ ] 日志搜索和过滤
- [ ] 操作统计图表
- [ ] 异常操作告警
- [ ] 日志归档
- [ ] 操作回放

## 🔗 相关文件

### 后端
- `database/operation_log.sql` - 数据库表结构
- `src/main/java/com/itheima/aspect/OperationLogAspect.java`
- `src/main/java/com/itheima/annotation/OperationLogger.java`
- `src/main/java/com/itheima/pojo/OperationLog.java`
- `src/main/java/com/itheima/mapper/OperationLogMapper.java`
- `src/main/java/com/itheima/controller/OperationLogController.java`

### 前端
- `tlias-vue-frontend/src/views/OperationLog.vue`
- `tlias-vue-frontend/src/api/log.js`
- `tlias-vue-frontend/src/router/index.js`
- `tlias-vue-frontend/src/layouts/MainLayout.vue`

## 💡 提示

操作日志功能已完全集成到系统中，无需额外配置即可使用。只需：

1. 执行数据库脚本
2. 重启后端服务
3. 登录管理员账号
4. 访问"操作日志"菜单

系统会自动记录所有标注了 `@OperationLogger` 的操作，并在前端实时展示！
