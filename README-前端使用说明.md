# 员工管理系统 - 使用说明

## 📦 项目已创建的文件

### 1. 前端界面
- **文件位置**: `src/main/resources/static/index.html`
- **功能**: 简洁的员工管理系统前端界面
- **特性**:
  - 用户登录（获取JWT Token）
  - 部门列表查询
  - 员工列表查询（支持条件筛选）
  - 响应式设计，美观的渐变UI

### 2. API测试文件
- **文件位置**: `src/test/api-test.http`
- **功能**: 完整的API接口测试用例
- **包含接口**:
  - 登录接口
  - 部门管理（增删改查）
  - 员工管理（增删改查、分页、条件查询）

## 🚀 快速开始

### 步骤1: 启动后端服务

```bash
# 使用 Maven 启动
mvn spring-boot:run

# 或者在 IDE 中直接运行
# TliasWebManagementApplication.java 主类
```

服务默认运行在: `http://localhost:8080`

### 步骤2: 访问前端界面

在浏览器中打开: `http://localhost:8080/index.html`

### 步骤3: 登录系统

1. 在登录页面输入用户名和密码
2. 默认测试账号（需要数据库中存在）:
   - 用户名: `admin`
   - 密码: `123456`
3. 登录成功后会显示 Token

### 步骤4: 使用功能

- 点击顶部导航切换到"部门管理"或"员工管理"
- 点击"刷新列表"或"查询"按钮加载数据

## 🧪 使用 API 测试文件

### 在 IntelliJ IDEA 中:

1. 打开 `src/test/api-test.http` 文件
2. 先运行"登录接口"，复制返回的 token
3. 将文件中所有 `YOUR_TOKEN_HERE` 替换为实际的 token
4. 点击接口旁边的绿色运行按钮测试

### 在 VS Code 中:

1. 安装插件: `REST Client`
2. 打开 `src/test/api-test.http` 文件
3. 按照上述步骤操作

### 使用 Postman:

1. 导入接口到 Postman
2. 设置 Header: `token: YOUR_TOKEN_HERE`
3. 发送请求测试

## ⚠️ 注意事项

### 1. 数据库准备

确保数据库中有测试数据，至少需要:

```sql
-- 创建测试用户（密码需要加密或明文存储）
INSERT INTO emp (username, password, name, gender, job, entrydate, dept_id, create_time, update_time)
VALUES ('admin', '123456', '管理员', 1, 1, '2020-01-01', 1, NOW(), NOW());

-- 创建测试部门
INSERT INTO dept (name, create_time, update_time)
VALUES ('技术部', NOW(), NOW());
```

### 2. 跨域问题

如果前端和后端不在同一端口，需要配置CORS。当前配置已支持同源访问。

### 3. Token 认证

- 所有接口（除了登录）都需要在请求头中携带 token
- Token 格式: `token: YOUR_JWT_TOKEN`
- 如果 token 过期或无效，会返回 401 错误

### 4. 拦截器配置

项目已配置登录拦截器，只有 `/login` 接口不需要 token，其他接口都需要。

## 📝 接口说明

### 登录接口
- **URL**: `POST /login`
- **参数**: `{"username": "admin", "password": "123456"}`
- **返回**: `{"code": 1, "data": "jwt_token"}`

### 部门接口
- `GET /depts` - 查询所有部门
- `GET /depts/{id}` - 根据ID查询
- `POST /depts` - 添加部门
- `PUT /depts` - 修改部门
- `DELETE /depts/{id}` - 删除部门

### 员工接口
- `GET /emps?page=1&pageSize=10` - 分页查询
- `GET /emps/{id}` - 根据ID查询
- `POST /emps` - 添加员工
- `PUT /emps` - 修改员工
- `DELETE /emps/{ids}` - 删除员工（支持批量）

## 🎨 前端界面特点

- 渐变紫色主题设计
- 响应式布局
- 实时消息提示
- Token 自动管理
- 简洁的表格展示

## 🔧 可能的问题

### 1. 前端无法访问
- 检查后端是否启动成功
- 确认访问地址: `http://localhost:8080/index.html`

### 2. 登录失败
- 检查数据库中是否有对应用户
- 确认用户名密码是否正确

### 3. 接口返回 401
- Token 未携带或已过期
- 重新登录获取新的 Token

### 4. 数据加载失败
- 检查数据库连接是否正常
- 查看后端控制台日志

## 📚 下一步

- 可以根据需要扩展前端功能（添加、编辑、删除操作）
- 优化UI设计
- 添加更多的表单验证
- 实现文件上传功能的前端界面
