# Vue 前后端分离改造方案

## 📋 改造概述

将当前的单体应用改造为**前后端分离架构**，前端使用 Vue 3 + Element Plus，后端保持 Spring Boot 不变。

## 🎯 改造目标

### 改造前（当前）
```
单体应用
├── 前端（HTML/CSS/JS）在 src/main/resources/static/
├── 后端（Spring Boot）
└── 部署在同一个 Tomcat，端口 8080
```

### 改造后
```
前后端分离
├── 前端项目（Vue 3）
│   ├── 独立的项目目录
│   ├── 使用 Vite 构建
│   └── 运行在端口 5173（开发）/ 80（生产）
│
└── 后端项目（Spring Boot）
    ├── 只提供 API 接口
    ├── 运行在端口 8080
    └── 配置 CORS 跨域
```

## 🛠️ 技术栈对比

| 层面 | 改造前 | 改造后 |
|------|--------|--------|
| 前端框架 | 原生 JavaScript | Vue 3 (Composition API) |
| UI 组件库 | 无 | Element Plus |
| 构建工具 | 无 | Vite |
| 路由管理 | 无 | Vue Router |
| 状态管理 | localStorage | Pinia |
| HTTP 请求 | Fetch API | Axios |
| 开发服务器 | Tomcat | Vite Dev Server |

## 📁 新的项目结构

```
tlias-system/
├── tlias-backend/              # 后端项目（现有项目）
│   ├── src/
│   ├── pom.xml
│   └── ...
│
└── tlias-frontend/             # 前端项目（新建）
    ├── public/                 # 静态资源
    ├── src/
    │   ├── api/                # API 接口封装
    │   │   ├── dept.js
    │   │   ├── emp.js
    │   │   ├── auth.js
    │   │   └── upload.js
    │   ├── assets/             # 资源文件
    │   ├── components/         # 公共组件
    │   │   ├── Header.vue
    │   │   ├── Sidebar.vue
    │   │   └── ...
    │   ├── views/              # 页面组件
    │   │   ├── Login.vue
    │   │   ├── Register.vue
    │   │   ├── Admin/
    │   │   │   ├── DeptManage.vue
    │   │   │   └── EmpManage.vue
    │   │   └── User/
    │   │       └── Profile.vue
    │   ├── router/             # 路由配置
    │   │   └── index.js
    │   ├── store/              # 状态管理
    │   │   └── index.js
    │   ├── utils/              # 工具函数
    │   │   ├── request.js      # Axios 封装
    │   │   └── auth.js         # 认证工具
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 🚀 改造步骤

### 阶段一：后端改造（必须）

#### 1. 配置 CORS 跨域

修改 `WebConfig.java`：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register", "/upload");
    }
    
    // 新增：配置 CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 允许所有来源（开发环境）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

#### 2. 移除静态资源映射

删除或注释掉 `addResourceHandlers` 方法，因为前端会独立部署。

#### 3. 调整拦截器

不再需要放行 HTML 文件：

```java
.excludePathPatterns("/login", "/register", "/upload")
```

### 阶段二：创建 Vue 前端项目

#### 1. 安装 Node.js

确保已安装 Node.js 16+ 和 npm：

```bash
node -v
npm -v
```

如果没有，从 https://nodejs.org/ 下载安装。

#### 2. 创建 Vue 项目

```bash
# 进入项目根目录
cd E:\a java\shijian1\daima

# 创建 Vue 项目
npm create vite@latest tlias-frontend -- --template vue

# 进入前端项目目录
cd tlias-frontend

# 安装依赖
npm install

# 安装其他依赖
npm install vue-router@4 pinia axios element-plus
npm install @element-plus/icons-vue
```

#### 3. 配置 Vite

创建 `vite.config.js`：

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
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

#### 4. 配置路由

创建 `src/router/index.js`：

```javascript
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Admin from '../views/Admin/Layout.vue'
import User from '../views/User/Profile.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/register',
    component: Register
  },
  {
    path: '/admin',
    component: Admin,
    meta: { requiresAuth: true, role: 1 }
  },
  {
    path: '/user',
    component: User,
    meta: { requiresAuth: true, role: 2 }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
```

#### 5. 封装 Axios

创建 `src/utils/request.js`：

```javascript
import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['token'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 1) {
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message)
    return Promise.reject(error)
  }
)

export default request
```

#### 6. 封装 API

创建 `src/api/auth.js`：

```javascript
import request from '@/utils/request'

export const login = (data) => {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export const register = (data) => {
  return request({
    url: '/register',
    method: 'post',
    data
  })
}
```

创建 `src/api/emp.js`：

```javascript
import request from '@/utils/request'

export const getEmpList = (params) => {
  return request({
    url: '/emps',
    method: 'get',
    params
  })
}

export const addEmp = (data) => {
  return request({
    url: '/emps',
    method: 'post',
    data
  })
}

// ... 其他接口
```

#### 7. 创建登录页面

创建 `src/views/Login.vue`：

```vue
<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>员工管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  
  try {
    const res = await login(form.value)
    localStorage.setItem('token', res.data)
    
    // 解析 token 获取角色
    const payload = JSON.parse(atob(res.data.split('.')[1]))
    
    ElMessage.success('登录成功')
    
    if (payload.role === 1) {
      router.push('/admin')
    } else {
      router.push('/user')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #667eea;
}

.el-button {
  width: 100%;
}

.register-link {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
}
</style>
```

#### 8. 配置 main.js

```javascript
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.mount('#app')
```

### 阶段三：运行和测试

#### 1. 启动后端

```bash
cd tlias-backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

#### 2. 启动前端

```bash
cd tlias-frontend
npm run dev
```

前端运行在 `http://localhost:5173`

#### 3. 测试

访问 `http://localhost:5173`，应该能看到 Vue 版本的登录页面。

## 📊 改造对比

### 开发体验

| 方面 | 改造前 | 改造后 |
|------|--------|--------|
| 热更新 | ❌ 需要重启 | ✅ 秒级热更新 |
| 组件化 | ❌ 无 | ✅ Vue 组件 |
| 代码复用 | ❌ 困难 | ✅ 组件复用 |
| 状态管理 | ❌ 手动管理 | ✅ Pinia |
| UI 组件 | ❌ 手写 | ✅ Element Plus |
| 路由管理 | ❌ 手动跳转 | ✅ Vue Router |

### 部署方式

**改造前：**
```bash
mvn clean package
java -jar target/tlias.jar
```

**改造后：**
```bash
# 前端
cd tlias-frontend
npm run build
# 将 dist 目录部署到 Nginx

# 后端
cd tlias-backend
mvn clean package
java -jar target/tlias.jar
```

## 🎯 我可以帮你做什么

### ✅ 我可以做的

1. **创建完整的 Vue 项目结构**
   - 所有配置文件
   - 路由配置
   - API 封装
   - 工具函数

2. **编写所有 Vue 组件**
   - 登录页面
   - 注册页面
   - 管理员后台
   - 普通用户页面
   - 所有功能组件

3. **后端 CORS 配置**
   - 修改 WebConfig
   - 调整拦截器

4. **提供完整的迁移指南**
   - 详细步骤
   - 代码示例
   - 常见问题解决

### ❌ 你需要做的

1. **安装 Node.js**
   - 从官网下载安装
   - 验证安装成功

2. **执行命令**
   - 创建 Vue 项目
   - 安装依赖
   - 启动开发服务器

3. **测试功能**
   - 验证前后端通信
   - 测试所有功能

4. **部署到生产环境**（可选）
   - 构建前端
   - 配置 Nginx
   - 部署后端

## 💡 建议

### 推荐改造方案

**方案一：完全改造**（推荐）
- 前端完全使用 Vue 3 + Element Plus
- 获得最佳开发体验
- 适合长期维护

**方案二：渐进式改造**
- 保留现有 HTML 页面
- 新功能使用 Vue 开发
- 逐步迁移旧页面

**方案三：保持现状**
- 继续使用原生 JavaScript
- 适合快速开发和学习

### 我的建议

如果你想学习现代前端开发，我**强烈推荐完全改造**。我可以：

1. 为你生成所有 Vue 组件代码
2. 提供详细的配置文件
3. 编写完整的 API 封装
4. 给出部署指南

你只需要：
1. 安装 Node.js
2. 复制我提供的代码
3. 执行几个命令
4. 测试功能

## 🚀 下一步

如果你决定改造，请告诉我：

1. **是否已安装 Node.js？**
2. **想要完全改造还是渐进式改造？**
3. **是否需要我生成所有 Vue 组件代码？**

我会根据你的选择提供相应的代码和指导！
