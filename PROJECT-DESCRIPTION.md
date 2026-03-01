# Tlias 员工管理系统 - 项目介绍

## 📋 项目概述

**tlias-web-management** 是一个基于 **Vue 3 + Spring Boot + MyBatis** 构建的现代化前后端分离员工与部门管理系统。系统采用完全前后端分离架构，提供完整的 RESTful API 接口，实现了部门管理、员工管理、用户认证、文件上传、操作日志等核心功能，并通过 JWT 实现无状态身份验证。

## 🏗️ 系统架构

### 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                    浏览器 (Browser)                      │
└─────────────────────────────────────────────────────────┘
                            │
                            │ HTTP/HTTPS
                            ▼
┌─────────────────────────────────────────────────────────┐
│              前端 (Vue 3 + Vite + Element Plus)          │
│  ┌──────────────────────────────────────────────────┐   │
│  │  • Vue Router (路由管理)                         │   │
│  │  • Pinia (状态管理)                              │   │
│  │  • Axios (HTTP 客户端)                           │   │
│  │  • Element Plus (UI 组件库)                      │   │
│  └──────────────────────────────────────────────────┘   │
│  开发端口: 5173 (Vite Dev Server)                       │
└─────────────────────────────────────────────────────────┘
                            │
                            │ REST API (JSON)
                            ▼
┌─────────────────────────────────────────────────────────┐
│              后端 (Spring Boot + MyBatis)                │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Controller (表现层)                             │   │
│  │       ↓                                          │   │
│  │  Service (业务逻辑层)                            │   │
│  │       ↓                                          │   │
│  │  Mapper (数据访问层)                             │   │
│  │       ↓                                          │   │
│  │  POJO (模型层)                                   │   │
│  └──────────────────────────────────────────────────┘   │
│  运行端口: 8080 (内置 Tomcat)                           │
└─────────────────────────────────────────────────────────┘
                            │
                            │ JDBC
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    MySQL 数据库                          │
└─────────────────────────────────────────────────────────┘
```

### 分层架构

#### 后端分层（Spring Boot）
- **表现层 (Controller)**：处理 HTTP 请求，参数验证，调用 Service
- **业务逻辑层 (Service)**：实现业务逻辑，事务管理
- **数据访问层 (Mapper)**：MyBatis 接口，执行 SQL 操作
- **模型层 (POJO)**：实体类、DTO、VO
- **切面层 (Aspect)**：AOP 切面，操作日志记录
- **拦截器 (Interceptor)**：登录验证，权限控制
- **配置层 (Config)**：CORS、Web、拦截器配置

#### 前端分层（Vue 3）
- **视图层 (Views)**：页面组件
- **布局层 (Layouts)**：页面布局组件
- **API 层 (API)**：接口封装，统一请求处理
- **状态管理 (Stores)**：Pinia 状态管理
- **路由层 (Router)**：路由配置，权限守卫
- **工具层 (Utils)**：公共工具函数

## 💻 技术栈

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.5 | 应用框架 |
| MyBatis | 2.2.2 | ORM 框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT | 0.9.1 | 身份认证 |
| 阿里云 OSS | 3.17.4 | 对象存储服务 |
| PageHelper | 1.4.2 | 分页插件 |
| Lombok | - | 简化 Java 代码 |
| FastJSON | 1.2.76 | JSON 处理 |
| Spring AOP | - | 面向切面编程 |
| Maven | - | 项目构建管理 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.0 | 渐进式 JavaScript 框架 |
| Vite | 5.0.0 | 下一代前端构建工具 |
| Vue Router | 4.2.5 | 官方路由管理器 |
| Pinia | 2.1.7 | Vue 状态管理库 |
| Element Plus | 2.5.0 | Vue 3 组件库 |
| Axios | 1.6.0 | HTTP 客户端 |

### 开发工具
- **IDE**：IntelliJ IDEA / VS Code
- **版本控制**：Git
- **API 测试**：Postman / REST Client
- **数据库工具**：MySQL Workbench / Navicat

## 🎯 核心功能

### 1. 用户认证与授权
- ✅ 用户登录（JWT Token）
- ✅ 用户注册
- ✅ 自动登录（Token 持久化）
- ✅ 退出登录
- ✅ 角色权限控制（管理员/普通用户）
- ✅ 路由守卫（前端）
- ✅ 登录拦截器（后端）

### 2. 部门管理（管理员）
- ✅ 查看部门列表
- ✅ 添加部门
- ✅ 编辑部门
- ✅ 删除部门
- ✅ 部门信息查询

### 3. 员工管理（管理员）
- ✅ 员工列表展示（分页）
- ✅ 多条件搜索（姓名、性别、入职日期范围）
- ✅ 添加员工
- ✅ 编辑员工信息
- ✅ 删除员工（支持批量）
- ✅ 员工详情查看
- ✅ 头像上传

### 4. 个人中心
- ✅ 查看个人信息
- ✅ 修改头像
- ✅ 查看同部门同事

### 5. 文件上传
- ✅ 图片上传（阿里云 OSS）
- ✅ 文件大小限制（10MB）
- ✅ 文件类型验证
- ✅ 上传进度显示

### 6. 操作日志（新增）
- ✅ 自动记录用户操作（AOP 切面）
- ✅ 实时日志展示
- ✅ 操作统计（总数、今日、成功、失败）
- ✅ 自动刷新（每5秒）
- ✅ 日志详情查看
- ✅ 密码自动隐藏

### 7. 数据可视化
- ✅ 首页统计卡片
- ✅ 员工统计（总数、性别分布）
- ✅ 部门统计
- ✅ 操作日志统计

## 🔐 安全特性

### 后端安全
- ✅ JWT Token 认证
- ✅ 登录拦截器（白名单机制）
- ✅ CORS 跨域配置
- ✅ 密码加密存储（建议）
- ✅ SQL 注入防护（MyBatis 预编译）
- ✅ 参数验证

### 前端安全
- ✅ 路由权限守卫
- ✅ Token 自动管理
- ✅ 请求拦截器（自动添加 Token）
- ✅ 响应拦截器（统一错误处理）
- ✅ XSS 防护（Vue 自动转义）

## 📊 数据处理

### 数据库操作
- **ORM 框架**：MyBatis
- **连接池**：HikariCP（Spring Boot 默认）
- **数据库**：MySQL 8.0+
- **命名映射**：驼峰命名自动映射（a_column → aColumn）
- **分页查询**：PageHelper 插件（物理分页）
- **事务管理**：Spring 声明式事务

### 数据展示
- **分页组件**：Element Plus Pagination
- **表格组件**：Element Plus Table
- **搜索过滤**：多条件组合查询
- **数据格式化**：日期、性别、职位等
- **空状态处理**：Empty 组件

### 统一响应格式
```java
{
    "code": 1,           // 1-成功, 0-失败
    "msg": "success",    // 消息
    "data": {...}        // 数据
}
```

## 🚀 项目特色

### 1. 完全前后端分离
- 前端和后端独立开发、独立部署
- 通过 REST API 通信
- 前端使用 Vite 开发服务器（热更新）
- 后端使用 Spring Boot 内置 Tomcat

### 2. 现代化技术栈
- Vue 3 Composition API
- Element Plus UI 组件库
- Pinia 状态管理
- Vite 快速构建

### 3. AOP 操作日志
- 使用 Spring AOP 自动记录操作
- 自定义注解 `@OperationLogger`
- 自动获取用户信息、IP 地址
- 自动隐藏敏感信息（密码）

### 4. 响应式设计
- 适配桌面端、平板端、移动端
- Element Plus 响应式组件
- 流畅的用户体验

### 5. 权限控制
- 前端路由守卫
- 后端拦截器
- 角色权限区分（管理员/普通用户）
- 数据权限隔离（部门）

### 6. 开发体验
- 热更新（前端）
- 自动重启（后端）
- 统一代码风格
- 完善的文档

## 📁 项目结构

```
tlias-web-management/
├── src/main/java/com/itheima/          # 后端 Java 代码
│   ├── annotation/                     # 自定义注解
│   ├── aspect/                         # AOP 切面
│   ├── config/                         # 配置类
│   ├── controller/                     # 控制器
│   ├── interceptor/                    # 拦截器
│   ├── mapper/                         # MyBatis Mapper
│   ├── pojo/                           # 实体类
│   ├── service/                        # 业务逻辑
│   └── utils/                          # 工具类
├── src/main/resources/
│   ├── application.properties          # 应用配置
│   ├── static/                         # 静态资源（旧版前端）
│   └── com/itheima/mapper/             # MyBatis XML
├── tlias-vue-frontend/                 # Vue 前端项目
│   ├── src/
│   │   ├── api/                        # API 接口
│   │   ├── assets/                     # 静态资源
│   │   ├── layouts/                    # 布局组件
│   │   ├── router/                     # 路由配置
│   │   ├── stores/                     # 状态管理
│   │   ├── views/                      # 页面组件
│   │   ├── App.vue                     # 根组件
│   │   └── main.js                     # 入口文件
│   ├── index.html                      # HTML 模板
│   ├── package.json                    # 依赖配置
│   └── vite.config.js                  # Vite 配置
├── database/                           # 数据库脚本
│   ├── tlias.sql                       # 主表结构
│   └── operation_log.sql               # 操作日志表
├── pom.xml                             # Maven 配置
└── README.md                           # 项目说明
```

## 🔄 数据流程

### 用户登录流程
```
1. 用户输入用户名密码
2. 前端发送 POST /login 请求
3. 后端验证用户名密码
4. 生成 JWT Token
5. 返回 Token 给前端
6. 前端存储 Token 到 localStorage
7. 后续请求自动携带 Token
8. 后端拦截器验证 Token
9. 解析 Token 获取用户信息
10. 执行业务逻辑
```

### 操作日志记录流程
```
1. 用户执行操作（如登录、添加员工）
2. 请求到达 Controller 方法（带 @OperationLogger 注解）
3. AOP 切面拦截方法执行
4. 提取操作信息（用户、IP、参数等）
5. 执行目标方法
6. 记录操作结果（成功/失败）
7. 保存日志到数据库
8. 前端定时查询日志
9. 实时展示操作记录
```

## 🎨 界面展示

### 页面列表
1. **登录页面** - 用户登录
2. **注册页面** - 用户注册
3. **首页** - 数据统计展示
4. **部门管理** - 部门 CRUD 操作
5. **员工管理** - 员工 CRUD、搜索、分页
6. **个人中心** - 个人信息、同事列表
7. **操作日志** - 操作记录、实时统计

## 📝 API 接口

### 认证接口
- `POST /login` - 用户登录
- `POST /register` - 用户注册

### 部门接口
- `GET /depts` - 查询所有部门
- `GET /depts/{id}` - 根据 ID 查询
- `POST /depts` - 添加部门
- `PUT /depts` - 修改部门
- `DELETE /depts/{id}` - 删除部门

### 员工接口
- `GET /emps` - 分页查询员工
- `GET /emps/{id}` - 根据 ID 查询
- `GET /emps/current` - 获取当前用户
- `POST /emps` - 添加员工
- `PUT /emps` - 修改员工
- `DELETE /emps/{ids}` - 删除员工
- `PUT /emps/avatar` - 更新头像

### 文件接口
- `POST /upload` - 文件上传

### 日志接口
- `GET /logs/recent` - 查询最近日志
- `GET /logs` - 查询所有日志

## 🚀 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 或 yarn

### 启动步骤

#### 1. 数据库准备
```bash
mysql -u root -p < database/tlias.sql
mysql -u root -p < database/operation_log.sql
```

#### 2. 配置数据库
编辑 `src/main/resources/application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tlias
spring.datasource.username=root
spring.datasource.password=your_password
```

#### 3. 启动后端
```bash
mvn spring-boot:run
```

#### 4. 启动前端
```bash
cd tlias-vue-frontend
npm install
npm run dev
```

#### 5. 访问系统
- 前端：http://localhost:5173
- 后端：http://localhost:8080

## 📚 文档

- [项目总览](PROJECT-OVERVIEW.md)
- [前后端分离部署说明](README-Vue前后端分离部署说明.md)
- [操作日志功能说明](README-操作日志功能说明.md)
- [故障排查指南](TROUBLESHOOTING.md)
- [部署检查清单](DEPLOYMENT-CHECKLIST.md)

## 🎓 学习价值

通过这个项目，你可以学习到：

1. **前后端分离架构**：理解前后端如何协作
2. **RESTful API 设计**：规范的接口设计
3. **JWT 认证机制**：无状态身份验证
4. **Spring Boot 开发**：快速构建 Web 应用
5. **MyBatis 使用**：ORM 框架实践
6. **Vue 3 开发**：现代化前端框架
7. **AOP 编程**：面向切面编程实践
8. **权限控制**：前后端权限管理
9. **文件上传**：阿里云 OSS 集成
10. **项目部署**：开发到生产的完整流程

## 🔧 后续优化

- [ ] 添加 TypeScript 支持
- [ ] 实现更细粒度的权限控制（RBAC）
- [ ] 添加单元测试和集成测试
- [ ] 优化性能（代码分割、懒加载）
- [ ] 添加国际化支持（i18n）
- [ ] 实现主题切换功能
- [ ] 添加数据可视化图表
- [ ] 实现实时通知功能（WebSocket）
- [ ] 添加导出功能（Excel）
- [ ] 实现数据备份和恢复

## 📄 许可证

MIT License

---

**项目状态**：✅ 生产就绪

**最后更新**：2024-02-25

**维护者**：[Your Name]
