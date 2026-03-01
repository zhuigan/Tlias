# 操作日志功能 - 部署检查清单

## 📋 部署前检查

- [ ] MySQL 已安装并运行
- [ ] Maven 已安装
- [ ] Node.js 已安装
- [ ] 数据库 `tlias` 已创建
- [ ] 数据库连接信息正确（`application.properties`）

## 🔧 部署步骤

### 1. 创建数据库表

```bash
mysql -u root -p tlias < database/operation_log.sql
```

**验证**：
```sql
USE tlias;
SHOW TABLES LIKE 'operation_log';
DESC operation_log;
```

- [ ] 表已创建
- [ ] 表结构正确

### 2. 添加 Maven 依赖

检查 `pom.xml` 是否包含：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

- [ ] AOP 依赖已添加

### 3. 编译项目

```bash
mvn clean install
```

- [ ] 编译成功
- [ ] 无错误信息

### 4. 启动后端

```bash
mvn spring-boot:run
```

**检查控制台输出**：
- [ ] 应用启动成功
- [ ] 端口 8080 已监听
- [ ] 无异常信息

### 5. 测试后端接口

#### 测试登录（会记录日志）
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

- [ ] 登录成功
- [ ] 控制台显示：`📝 操作日志: admin - 用户登录 - 成功`

#### 测试日志查询
```bash
curl http://localhost:8080/logs/recent?limit=10 \
  -H "token: YOUR_TOKEN_HERE"
```

- [ ] 返回日志列表
- [ ] 包含刚才的登录记录

### 6. 启动前端

```bash
cd tlias-vue-frontend
npm install  # 如果还没安装依赖
npm run dev
```

- [ ] 前端启动成功
- [ ] 访问 http://localhost:5173

### 7. 测试前端功能

#### 登录系统
- [ ] 使用管理员账号登录
- [ ] 登录成功

#### 访问操作日志页面
- [ ] 左侧菜单显示"操作日志"
- [ ] 点击进入操作日志页面
- [ ] 页面正常加载

#### 检查日志显示
- [ ] 统计卡片显示正确
- [ ] 日志列表显示记录
- [ ] 可以看到刚才的登录记录

#### 测试自动刷新
- [ ] 自动刷新开关可用
- [ ] 开启后每5秒刷新
- [ ] 关闭后停止刷新

#### 测试手动刷新
- [ ] 点击刷新按钮
- [ ] 日志列表更新

### 8. 测试各种操作

#### 注册新用户
- [ ] 访问注册页面
- [ ] 填写信息并注册
- [ ] 操作日志中出现"用户注册"记录

#### 上传头像
- [ ] 进入个人中心
- [ ] 上传头像
- [ ] 操作日志中出现"上传文件"记录
- [ ] 操作日志中出现"更新头像"记录

#### 员工管理操作（管理员）
- [ ] 添加员工 → 日志记录"添加员工"
- [ ] 修改员工 → 日志记录"修改员工信息"
- [ ] 删除员工 → 日志记录"删除员工"

## ✅ 功能验证

### 日志记录验证

- [ ] 用户名正确显示
- [ ] 操作类型正确
- [ ] IP 地址正确
- [ ] 操作时间正确
- [ ] 请求参数显示（密码已隐藏）
- [ ] 操作结果正确（成功/失败）

### 界面验证

- [ ] 统计卡片数据正确
- [ ] 日志列表显示完整
- [ ] 颜色标签正确
- [ ] 长参数可悬停查看
- [ ] 响应式设计正常

### 权限验证

- [ ] 管理员可以访问操作日志
- [ ] 普通用户无法访问操作日志
- [ ] 未登录用户被重定向到登录页

## 🐛 故障排查

### 问题1：日志表不存在

**症状**：
```
Table 'tlias.operation_log' doesn't exist
```

**解决**：
```bash
mysql -u root -p tlias < database/operation_log.sql
```

### 问题2：AOP 不生效

**症状**：操作没有被记录

**检查**：
1. `pom.xml` 是否有 AOP 依赖
2. 方法上是否有 `@OperationLogger` 注解
3. 重新编译：`mvn clean install`
4. 重启后端服务

### 问题3：前端无法加载日志

**症状**：页面空白或报错

**检查**：
1. 是否使用管理员账号登录
2. 浏览器控制台是否有错误
3. 网络请求是否成功
4. Token 是否有效

### 问题4：日志没有用户名

**症状**：用户名显示"匿名用户"

**原因**：Token 未正确传递或解析失败

**检查**：
1. 请求头是否包含 token
2. Token 是否有效
3. JWT 配置是否正确

## 📊 性能检查

### 数据库性能

```sql
-- 检查索引
SHOW INDEX FROM operation_log;

-- 检查表大小
SELECT 
    table_name,
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS "Size (MB)"
FROM information_schema.TABLES
WHERE table_schema = 'tlias' AND table_name = 'operation_log';

-- 检查记录数
SELECT COUNT(*) FROM operation_log;
```

- [ ] 索引已创建
- [ ] 表大小合理
- [ ] 查询速度正常

### 应用性能

- [ ] 日志记录不影响主流程性能
- [ ] 前端加载速度正常
- [ ] 自动刷新不卡顿

## 📝 维护建议

### 定期清理

建议每月清理一次旧日志：

```sql
-- 删除30天前的日志
DELETE FROM operation_log 
WHERE operate_time < DATE_SUB(NOW(), INTERVAL 30 DAY);
```

### 备份

定期备份操作日志：

```bash
mysqldump -u root -p tlias operation_log > operation_log_backup_$(date +%Y%m%d).sql
```

### 监控

- [ ] 监控日志表大小
- [ ] 监控失败操作数量
- [ ] 关注异常操作

## 🎉 部署完成

如果所有检查项都通过，恭喜你！操作日志功能已成功部署！

### 下一步

1. 通知团队成员新功能上线
2. 培训管理员如何使用
3. 定期查看操作日志
4. 根据需要调整配置

### 文档参考

- 详细使用说明：`README-操作日志功能说明.md`
- 功能总结：`OPERATION-LOG-SUMMARY.md`
- 项目总览：`PROJECT-OVERVIEW.md`

---

**部署日期**：_____________

**部署人员**：_____________

**验证人员**：_____________

**备注**：_____________
