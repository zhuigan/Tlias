# Tlias 员工管理系统 - 项目总览

## 🎯 项目简介

Tlias 员工管理系统是一个基于 **Vue 3 + Spring Boot** 的前后端分离项目，提供完整的员工和部门管理功能。

## 📊 改造对比

### 改造前（原始架构）
- ❌ 前后端耦合（HTML 嵌入在 Spring Boot 项目中）
- ❌ 使用原生 JavaScript + 简单 CSS
- ❌ 代码维护困难
- ❌ 无法独立部署前端
- ❌ 开发效率低

### 改造后（现代化架构）
- ✅ 完全前后端分离
- ✅ Vue 3 + Element Plus 现代化 UI
- ✅ 组件化开发，代码可维护性高
- ✅ 前后端可独立开发和部署
- ✅ 开发效率大幅提升
- ✅ 用户体验更好

## 🏗️ 技术架构

```
┌─────────────────────────────────────────────────────────┐
│                      浏览器 (Browser)                     │
└─────────────────────────────────────────────────────────┘
                            │
                            │ HTTP/HTTPS
                            ▼
┌─────────────────────────────────────────────────────────┐
│                  前端 (Vue 3 Frontend)                    │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Vue 3 + Vite + Vue Router + Pinia              │   │
│  │  Element Plus + Axios                            │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                            │
                            │ REST API (JSON)
                            ▼
┌─────────────────────────────────────────────────────────┐
│                 后端 (Spring Boot Backend)                │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Spring Boot + Spring MVC                        │   │
│  │  MyBatis + JWT + CORS                            │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                            │
                            │ JDBC
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    数据库 (MySQL)                         │
└─────────────────────────────────────────────────────────┘
```

## 📁 项目结构

```
tlias-web-management/
│
├── 📂 src/main/java/com/itheima/          # 后端 Java 代码
│   ├── config/                            # 配置类
│   │   ├── CorsConfig.java               # ✨ 跨域配置（新增）
│   │   └── WebConfig.java                # Web 配置
│   ├── controller/                        # 控制器
│   │   ├── LoginController.java          # 登录接口
│   │   ├── RegisterController.java       # 注册接口
│   │   ├── DeptController.java           # 部门接口
│   │   ├── EmpController.java            # 员工接口
│   │   └── FileController.java           # 文件上传接口
│   ├── service/                           # 业务逻辑层
│   ├── mapper/                            # 数据访问层
│   ├── pojo/                              # 实体类
│   ├── utils/                             # 工具类
│   └── interceptor/                       # 拦截器
│
├── 📂 src/main/resources/
│   ├── application.properties             # 应用配置
│   └── static/                            # 静态资源（旧版前端，可保留）
│
├── 📂 tlias-vue-frontend/                 # ✨ Vue 前端项目（新增）
│   ├── src/
│   │   ├── api/                          # API 接口封装
│   │   │   ├── request.js               # Axios 封装
│   │   │   ├── auth.js                  # 认证接口
│   │   │   ├── dept.js                  # 部门接口
│   │   │   └── emp.js                   # 员工接口
│   │   ├── views/                        # 页面组件
│   │   │   ├── Login.vue                # 登录页
│   │   │   ├── Register.vue             # 注册页
│   │   │   ├── Dashboard.vue            # 首页
│   │   │   ├── Dept.vue                 # 部门管理
│   │   │   ├── Emp.vue                  # 员工管理
│   │   │   └── Profile.vue              # 个人中心
│   │   ├── layouts/                      # 布局组件
│   │   │   └── MainLayout.vue           # 主布局
│   │   ├── router/                       # 路由配置
│   │   │   └── index.js                 # 路由定义
│   │   ├── stores/                       # 状态管理
│   │   │   └── user.js                  # 用户状态
│   │   ├── App.vue                       # 根组件
│   │   └── main.js                       # 入口文件
│   ├── package.json                      # 依赖配置
│   ├── vite.config.js                    # Vite 配置
│   └── README.md                         # 前端文档
│
├── 📄 pom.xml                             # Maven 配置
├── 📄 start-dev.sh                        # ✨ 启动脚本（新增）
├── 📄 start-dev.bat                       # ✨ Windows 启动脚本（新增）
├── 📄 stop-dev.sh                         # ✨ 停止脚本（新增）
├── 📄 README-Vue前后端分离部署说明.md      # ✨ 部署文档（新增）
└── 📄 PROJECT-OVERVIEW.md                 # ✨ 项目总览（本文件）
```

## 🚀 快速开始

### 方式一：使用启动脚本（推荐）

#### Windows
```bash
# 双击运行或命令行执行
start-dev.bat
```

#### Linux/Mac
```bash
# 添加执行权限
chmod +x start-dev.sh stop-dev.sh

# 启动服务
./start-dev.sh

# 停止服务
./stop-dev.sh
```

### 方式二：手动启动

#### 1. 启动后端
```bash
# 在项目根目录
mvn spring-boot:run
```

#### 2. 启动前端
```bash
# 进入前端目录
cd tlias-vue-frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

#### 3. 访问系统
- 前端: http://localhost:5173
- 后端: http://localhost:8080

## 🎨 功能模块

### 1. 用户认证
- ✅ 用户登录（JWT Token）
- ✅ 用户注册
- ✅ 自动登录（Token 持久化）
- ✅ 退出登录

### 2. 部门管理（管理员）
- ✅ 查看部门列表
- ✅ 添加部门
- ✅ 编辑部门
- ✅ 删除部门

### 3. 员工管理（管理员）
- ✅ 查看员工列表（分页）
- ✅ 搜索员工（姓名、性别、入职日期）
- ✅ 添加员工
- ✅ 编辑员工
- ✅ 删除员工
- ✅ 上传员工头像

### 4. 个人中心
- ✅ 查看个人信息
- ✅ 修改头像
- ✅ 查看同部门同事

### 5. 权限控制
- ✅ 路由权限（管理员/普通用户）
- ✅ 接口权限（JWT Token）
- ✅ 数据权限（部门隔离）

## 🔧 技术栈详情

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.0 | 渐进式 JavaScript 框架 |
| Vite | 5.0.0 | 下一代前端构建工具 |
| Vue Router | 4.2.5 | 官方路由管理器 |
| Pinia | 2.1.7 | Vue 状态管理库 |
| Element Plus | 2.5.0 | Vue 3 组件库 |
| Axios | 1.6.0 | HTTP 客户端 |

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.5 | Java 应用框架 |
| MyBatis | 2.2.2 | 持久层框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT | 0.9.1 | Token 认证 |
| Lombok | - | 简化 Java 代码 |

## 📡 API 接口

### 认证接口
```
POST   /login              # 用户登录
POST   /register           # 用户注册
```

### 部门接口
```
GET    /depts              # 查询所有部门
GET    /depts/{id}         # 根据ID查询
POST   /depts              # 添加部门
PUT    /depts              # 修改部门
DELETE /depts/{id}         # 删除部门
```

### 员工接口
```
GET    /emps               # 分页查询员工
GET    /emps/{id}          # 根据ID查询
GET    /emps/current       # 获取当前用户
POST   /emps               # 添加员工
PUT    /emps               # 修改员工
DELETE /emps/{ids}         # 删除员工
PUT    /emps/avatar        # 更新头像
```

### 文件接口
```
POST   /upload             # 文件上传
```

## 🎯 核心特性

### 1. 前后端分离
- 前端和后端完全解耦
- 独立开发、独立部署
- 通过 REST API 通信

### 2. 组件化开发
- Vue 单文件组件
- 可复用的 UI 组件
- 清晰的代码结构

### 3. 状态管理
- Pinia 集中式状态管理
- 用户信息持久化
- Token 自动管理

### 4. 路由管理
- Vue Router 路由控制
- 路由守卫（权限验证）
- 懒加载优化

### 5. 跨域支持
- 后端 CORS 配置
- 前端代理配置
- 支持跨域请求

### 6. 响应式设计
- Element Plus 响应式组件
- 适配不同屏幕尺寸
- 良好的移动端体验

## 📦 部署方案

### 开发环境
```bash
# 前端: http://localhost:5173
# 后端: http://localhost:8080
# 通过 Vite 代理转发请求
```

### 生产环境

#### 方案一：分离部署（推荐）
```
前端: Nginx 部署静态文件
后端: Java -jar 运行 Spring Boot
数据库: MySQL 独立部署
```

#### 方案二：集成部署
```
将前端构建产物放入后端 static 目录
统一通过 Spring Boot 提供服务
```

详见: [README-Vue前后端分离部署说明.md](./README-Vue前后端分离部署说明.md)

## 🔐 安全特性

- ✅ JWT Token 认证
- ✅ 密码加密存储（建议）
- ✅ CORS 跨域配置
- ✅ 请求拦截器
- ✅ 路由权限控制
- ✅ 接口权限验证

## 📈 性能优化

- ✅ 路由懒加载
- ✅ 组件按需引入
- ✅ Vite 快速构建
- ✅ 生产环境代码压缩
- ✅ 静态资源缓存

## 🐛 常见问题

### 1. 前端无法连接后端
**解决方案**: 
- 检查后端是否启动
- 确认端口号正确
- 查看 Vite 代理配置

### 2. Token 失效
**解决方案**:
- 重新登录获取新 Token
- 检查 JWT 配置
- 确认 Token 存储正确

### 3. 跨域问题
**解决方案**:
- 确认后端 CorsConfig 配置
- 检查前端请求头
- 查看浏览器控制台错误

### 4. 文件上传失败
**解决方案**:
- 检查文件大小限制
- 确认 OSS 配置
- 查看后端日志

## 📚 相关文档

- [前端 README](./tlias-vue-frontend/README.md)
- [部署说明](./README-Vue前后端分离部署说明.md)
- [原前端使用说明](./README-前端使用说明.md)

## 🎓 学习资源

### Vue 3
- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Vue Router 文档](https://router.vuejs.org/zh/)
- [Pinia 文档](https://pinia.vuejs.org/zh/)

### Element Plus
- [Element Plus 官方文档](https://element-plus.org/zh-CN/)

### Spring Boot
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis 文档](https://mybatis.org/mybatis-3/zh/)

## 🚧 后续优化

- [ ] 添加 TypeScript 支持
- [ ] 实现更细粒度的权限控制
- [ ] 添加单元测试
- [ ] 优化性能（代码分割、懒加载）
- [ ] 添加国际化支持
- [ ] 实现主题切换
- [ ] 添加数据可视化
- [ ] 实现实时通知

## 📝 更新日志

### v2.0.0 (2024-02-25)
- ✨ 完全改造为 Vue 3 前后端分离架构
- ✨ 使用 Element Plus UI 组件库
- ✨ 添加 Pinia 状态管理
- ✨ 添加 Vue Router 路由管理
- ✨ 优化用户体验和界面设计
- ✨ 添加跨域支持
- ✨ 添加启动脚本
- ✨ 完善项目文档

### v1.0.0 (之前)
- 基础的 Spring Boot + HTML 项目
- 员工和部门管理功能
- JWT 认证

## 👥 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

MIT License

---

**项目改造完成！** 🎉

现在你拥有一个现代化的前后端分离项目，具有更好的可维护性、可扩展性和用户体验。
