# Tlias 员工管理系统 - Vue 前端

基于 Vue 3 + Vite + Element Plus 的前后端分离项目前端部分。

## 技术栈

- Vue 3 - 渐进式 JavaScript 框架
- Vite - 下一代前端构建工具
- Vue Router - 官方路由管理器
- Pinia - Vue 状态管理库
- Element Plus - 基于 Vue 3 的组件库
- Axios - HTTP 客户端

## 功能特性

- 🔐 用户登录/注册
- 👥 员工管理（增删改查、分页、搜索）
- 🏢 部门管理（增删改查）
- 👤 个人中心（查看信息、修改头像）
- 🎨 响应式设计
- 🔒 路由权限控制
- 📦 状态管理

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:5173

### 生产构建

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 项目结构

```
tlias-vue-frontend/
├── src/
│   ├── api/              # API 接口
│   │   ├── auth.js       # 认证接口
│   │   ├── dept.js       # 部门接口
│   │   ├── emp.js        # 员工接口
│   │   └── request.js    # Axios 封装
│   ├── layouts/          # 布局组件
│   │   └── MainLayout.vue
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── stores/           # 状态管理
│   │   └── user.js
│   ├── views/            # 页面组件
│   │   ├── Dashboard.vue # 首页
│   │   ├── Dept.vue      # 部门管理
│   │   ├── Emp.vue       # 员工管理
│   │   ├── Login.vue     # 登录
│   │   ├── Profile.vue   # 个人中心
│   │   └── Register.vue  # 注册
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── index.html            # HTML 模板
├── package.json          # 项目配置
└── vite.config.js        # Vite 配置
```

## 后端配置

确保后端服务运行在 `http://localhost:8080`

前端通过 Vite 代理转发 `/api` 请求到后端。

## 默认账号

- 管理员账号：admin / 123456
- 普通用户：根据数据库配置

## 注意事项

1. 确保后端服务已启动
2. 确保数据库连接正常
3. Token 存储在 localStorage 中
4. 文件上传需要配置阿里云 OSS 或本地存储

## 开发建议

- 使用 Vue DevTools 进行调试
- 遵循 Vue 3 Composition API 规范
- 保持组件的单一职责原则
- 合理使用 Pinia 进行状态管理
