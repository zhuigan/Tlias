# Tlias 员工管理系统 - Vue 前后端分离部署说明

## 项目架构

本项目已完全改造为前后端分离架构：

- **后端**: Spring Boot (端口 8080)
- **前端**: Vue 3 + Vite (开发端口 5173)

## 目录结构

```
tlias-web-management/
├── src/                          # 后端 Spring Boot 代码
│   └── main/
│       ├── java/
│       └── resources/
├── tlias-vue-frontend/           # 前端 Vue 项目（新增）
│   ├── src/
│   │   ├── api/                  # API 接口封装
│   │   ├── views/                # 页面组件
│   │   ├── layouts/              # 布局组件
│   │   ├── router/               # 路由配置
│   │   ├── stores/               # 状态管理
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
└── pom.xml                       # 后端 Maven 配置
```

## 快速启动

### 1. 启动后端服务

```bash
# 在项目根目录
mvn spring-boot:run

# 或在 IDE 中运行
# TliasWebManagementApplication.java
```

后端服务将运行在: `http://localhost:8080`

### 2. 启动前端服务

```bash
# 进入前端目录
cd tlias-vue-frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

前端服务将运行在: `http://localhost:5173`

### 3. 访问系统

在浏览器中打开: `http://localhost:5173`

## 功能对比

### 原有功能（已保留）

✅ 用户登录/注册
✅ 部门管理（增删改查）
✅ 员工管理（增删改查、分页、搜索）
✅ 个人中心
✅ 文件上传
✅ JWT 认证
✅ 角色权限控制

### 新增特性

🆕 完全前后端分离架构
🆕 Vue 3 Composition API
🆕 Element Plus UI 组件库
🆕 Pinia 状态管理
🆕 Vue Router 路由管理
🆕 响应式设计
🆕 更好的用户体验
🆕 代码模块化和可维护性

## 技术栈

### 后端
- Spring Boot 2.7.5
- MyBatis
- MySQL
- JWT
- 阿里云 OSS

### 前端
- Vue 3.4
- Vite 5.0
- Vue Router 4.2
- Pinia 2.1
- Element Plus 2.5
- Axios 1.6

## API 接口

所有接口保持不变，前端通过 Axios 调用：

### 认证接口
- `POST /login` - 用户登录
- `POST /register` - 用户注册

### 部门接口
- `GET /depts` - 查询所有部门
- `GET /depts/{id}` - 根据ID查询
- `POST /depts` - 添加部门
- `PUT /depts` - 修改部门
- `DELETE /depts/{id}` - 删除部门

### 员工接口
- `GET /emps` - 分页查询员工
- `GET /emps/{id}` - 根据ID查询
- `GET /emps/current` - 获取当前用户信息
- `POST /emps` - 添加员工
- `PUT /emps` - 修改员工
- `DELETE /emps/{ids}` - 删除员工
- `PUT /emps/avatar` - 更新头像

### 文件接口
- `POST /upload` - 文件上传

## 跨域配置

后端已配置 CORS 支持，允许前端跨域访问：

```java
// src/main/java/com/itheima/config/CorsConfig.java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // 允许所有域名、请求头、请求方法
        // 生产环境建议指定具体域名
    }
}
```

## 前端代理配置

开发环境下，Vite 配置了代理转发：

```javascript
// tlias-vue-frontend/vite.config.js
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

前端请求 `/api/login` 会被代理到 `http://localhost:8080/login`

## 生产环境部署

### 1. 构建前端

```bash
cd tlias-vue-frontend
npm run build
```

构建产物在 `tlias-vue-frontend/dist` 目录

### 2. 部署方案

#### 方案一：分离部署（推荐）

**前端部署**:
- 使用 Nginx 部署前端静态文件
- 配置反向代理到后端 API

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /path/to/tlias-vue-frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # 代理后端 API
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

**后端部署**:
```bash
mvn clean package
java -jar target/tlias-web-management-0.0.1-SNAPSHOT.jar
```

#### 方案二：集成部署

将前端构建产物复制到后端 `src/main/resources/static` 目录：

```bash
cd tlias-vue-frontend
npm run build
cp -r dist/* ../src/main/resources/static/
cd ..
mvn clean package
java -jar target/tlias-web-management-0.0.1-SNAPSHOT.jar
```

访问: `http://localhost:8080`

## 环境变量配置

### 前端环境变量

创建 `.env.development` 和 `.env.production`:

```bash
# .env.development
VITE_API_BASE_URL=/api

# .env.production
VITE_API_BASE_URL=https://your-api-domain.com
```

### 后端配置

修改 `application.properties`:

```properties
# 生产环境数据库配置
spring.datasource.url=jdbc:mysql://your-db-host:3306/tlias
spring.datasource.username=your-username
spring.datasource.password=your-password

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 常见问题

### 1. 前端无法连接后端

- 检查后端是否启动: `http://localhost:8080`
- 检查 Vite 代理配置
- 查看浏览器控制台网络请求

### 2. 登录后 Token 失效

- 检查 JWT 配置
- 确认 Token 存储在 localStorage
- 查看请求头是否携带 token

### 3. 文件上传失败

- 检查阿里云 OSS 配置
- 确认文件大小限制
- 查看后端日志

### 4. 跨域问题

- 确认后端 CorsConfig 配置正确
- 检查前端请求是否携带凭证
- 查看浏览器控制台错误信息

## 开发建议

1. **前端开发**: 使用 `npm run dev` 启动开发服务器，支持热更新
2. **后端开发**: 使用 IDE 的 Spring Boot 运行配置，支持热部署
3. **API 测试**: 使用 Postman 或 `src/test/api-test.http` 测试接口
4. **代码规范**: 遵循 Vue 3 和 Spring Boot 最佳实践
5. **版本控制**: 前后端代码分别管理，使用 Git 分支策略

## 项目优势

✨ **前后端分离**: 独立开发、独立部署、独立扩展
✨ **技术栈现代化**: Vue 3 + Vite + Element Plus
✨ **开发体验**: 热更新、组件化、类型提示
✨ **用户体验**: 响应式设计、流畅交互
✨ **可维护性**: 代码模块化、职责清晰
✨ **可扩展性**: 易于添加新功能和页面

## 后续优化建议

- [ ] 添加 TypeScript 支持
- [ ] 实现更细粒度的权限控制
- [ ] 添加单元测试和 E2E 测试
- [ ] 优化性能（懒加载、代码分割）
- [ ] 添加国际化支持
- [ ] 实现主题切换功能
- [ ] 添加数据可视化图表
- [ ] 实现实时通知功能

## 联系方式

如有问题，请查看项目文档或提交 Issue。
