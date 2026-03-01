# 修复操作日志 500 错误

## 问题诊断

前端显示 "Request failed with status code 500"，这通常是因为：

1. ❌ 数据库表 `operation_log` 不存在
2. ❌ 后端服务未正确启动
3. ❌ AOP 依赖未加载

## 快速修复步骤

### 步骤 1：检查数据库表

打开 MySQL 命令行或工具，执行：

```sql
USE tlias;
SHOW TABLES LIKE 'operation_log';
```

**如果没有返回结果**，说明表不存在，需要创建。

### 步骤 2：创建数据库表

#### 方式一：使用命令行（推荐）

```bash
mysql -u root -p tlias < database/operation_log.sql
```

输入密码后回车。

#### 方式二：手动执行 SQL

打开 `database/operation_log.sql` 文件，复制内容到 MySQL 工具中执行：

```sql
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

CREATE INDEX idx_username ON operation_log(username);
CREATE INDEX idx_operate_time ON operation_log(operate_time);
```

### 步骤 3：验证表已创建

```sql
DESC operation_log;
```

应该看到表结构。

### 步骤 4：重启后端服务

**停止当前服务**：
- 在运行 `mvn spring-boot:run` 的终端按 `Ctrl+C`

**重新启动**：
```bash
mvn clean spring-boot:run
```

### 步骤 5：刷新前端页面

在浏览器中刷新操作日志页面（F5）。

## 验证修复

### 1. 检查后端日志

后端控制台应该没有错误信息，并且能看到：
```
Started TliasWebManagementApplication
```

### 2. 测试登录

重新登录系统，后端控制台应该显示：
```
📝 操作日志: admin - 用户登录 - 成功
```

### 3. 查看操作日志页面

访问"操作日志"菜单，应该能看到：
- 统计卡片显示数据
- 日志列表显示记录
- 没有错误提示

## 如果还是 500 错误

### 查看详细错误信息

1. **查看后端控制台**：
   - 找到完整的错误堆栈信息
   - 通常会显示具体是什么错误

2. **常见错误及解决方案**：

#### 错误 1：Table doesn't exist
```
Table 'tlias.operation_log' doesn't exist
```
**解决**：执行步骤 2 创建表

#### 错误 2：Column not found
```
Unknown column 'xxx' in 'field list'
```
**解决**：删除表重新创建
```sql
DROP TABLE IF EXISTS operation_log;
-- 然后重新执行创建表的 SQL
```

#### 错误 3：AOP 相关错误
```
No qualifying bean of type 'OperationLogMapper'
```
**解决**：
1. 确认 `pom.xml` 中有 AOP 依赖
2. 重新编译：`mvn clean install`
3. 重启服务

#### 错误 4：MyBatis 映射错误
```
Invalid bound statement
```
**解决**：
1. 检查 `@Mapper` 注解是否存在
2. 检查 SQL 语法是否正确
3. 重新编译项目

## 临时禁用操作日志（如果急需使用系统）

如果需要临时禁用操作日志功能，可以：

### 方法 1：注释掉 AOP 切面

编辑 `src/main/java/com/itheima/aspect/OperationLogAspect.java`：

在类上添加注释：
```java
// @Aspect
// @Component
public class OperationLogAspect {
    // ...
}
```

### 方法 2：移除控制器上的注解

临时移除控制器方法上的 `@OperationLogger` 注解。

### 方法 3：修改前端路由

编辑 `tlias-vue-frontend/src/router/index.js`，临时注释掉操作日志路由：

```javascript
// {
//   path: 'logs',
//   name: 'OperationLog',
//   component: () => import('@/views/OperationLog.vue'),
//   meta: { requiresAdmin: true }
// }
```

## 完整的诊断命令

```bash
# 1. 检查数据库
mysql -u root -p -e "USE tlias; SHOW TABLES LIKE 'operation_log';"

# 2. 如果表不存在，创建它
mysql -u root -p tlias < database/operation_log.sql

# 3. 验证表已创建
mysql -u root -p -e "USE tlias; DESC operation_log;"

# 4. 重新编译项目
mvn clean install

# 5. 启动后端
mvn spring-boot:run
```

## 需要帮助？

如果以上步骤都无法解决问题，请提供：

1. 后端控制台的完整错误信息
2. 浏览器控制台的错误信息（F12 → Console）
3. 数据库中是否有 `operation_log` 表

这样我可以提供更具体的解决方案。
