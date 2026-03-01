# Nginx 使用说明

## 📌 重要提示

**当前项目不需要 Nginx 也能正常运行！**

- 前端和后端在同一个 Spring Boot 项目中
- 直接访问 `http://localhost:8080/index.html` 即可
- 这个文档仅供学习和生产环境部署参考

## 🤔 什么时候需要 Nginx？

### 场景1：前后端分离部署
- 前端独立部署（Vue/React 项目）
- 需要反向代理到后端 API
- 需要处理跨域问题

### 场景2：生产环境
- 负载均衡（多个后端实例）
- 静态资源缓存优化
- HTTPS 证书管理
- 安全防护（限流、防 DDoS）

### 场景3：微服务架构
- 统一入口网关
- 路由分发到不同服务
- 服务发现和健康检查

## 🚀 如何使用 Nginx（可选）

### Windows 安装

**方法1：直接下载**
1. 访问 http://nginx.org/en/download.html
2. 下载 Windows 版本（如 nginx-1.24.0.zip）
3. 解压到任意目录（如 `C:\nginx`）

**方法2：使用包管理器**
```bash
# 使用 Chocolatey
choco install nginx

# 使用 Scoop
scoop install nginx
```

### 配置步骤

**1. 复制配置文件**
```bash
# 将项目中的 nginx.conf 复制到 nginx 安装目录
# 或者修改 nginx/conf/nginx.conf
```

**2. 修改配置**
根据实际情况修改：
- 端口号（默认 80）
- 后端地址（默认 127.0.0.1:8080）
- 静态资源路径

**3. 启动 Nginx**
```bash
# 进入 nginx 目录
cd C:\nginx

# 启动
nginx.exe

# 或者使用 start 命令
start nginx
```

**4. 测试配置**
```bash
# 测试配置文件是否正确
nginx -t

# 重新加载配置
nginx -s reload

# 停止 nginx
nginx -s stop

# 快速停止
nginx -s quit
```

**5. 访问应用**
```
浏览器访问: http://localhost
Nginx 会代理到: http://localhost:8080
```

## 📊 架构对比

### 不使用 Nginx（当前）
```
浏览器 → http://localhost:8080 → Spring Boot
                                    ├── 静态资源
                                    └── API 接口
```

### 使用 Nginx
```
浏览器 → http://localhost:80 → Nginx → http://localhost:8080 → Spring Boot
                                 ├── 静态资源缓存
                                 ├── 负载均衡
                                 └── 反向代理
```

## 🔧 常用配置示例

### 1. 基础反向代理
```nginx
location / {
    proxy_pass http://localhost:8080;
}
```

### 2. 负载均衡
```nginx
upstream backend {
    server 127.0.0.1:8080 weight=3;
    server 127.0.0.1:8081 weight=1;
}

location / {
    proxy_pass http://backend;
}
```

### 3. 静态资源优化
```nginx
location /static/ {
    root /var/www/tlias;
    expires 30d;
    gzip on;
    gzip_types text/css application/javascript;
}
```

### 4. HTTPS 配置
```nginx
server {
    listen 443 ssl;
    server_name example.com;
    
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    location / {
        proxy_pass http://localhost:8080;
    }
}
```

### 5. 跨域配置
```nginx
location /api/ {
    add_header Access-Control-Allow-Origin *;
    add_header Access-Control-Allow-Methods 'GET, POST, PUT, DELETE';
    add_header Access-Control-Allow-Headers 'token, Content-Type';
    
    if ($request_method = 'OPTIONS') {
        return 204;
    }
    
    proxy_pass http://localhost:8080/;
}
```

## 🎯 生产环境最佳实践

### 1. 安全配置
```nginx
# 隐藏版本号
server_tokens off;

# 限制请求大小
client_max_body_size 10M;

# 限流
limit_req_zone $binary_remote_addr zone=one:10m rate=10r/s;
limit_req zone=one burst=20;
```

### 2. 性能优化
```nginx
# 开启 gzip 压缩
gzip on;
gzip_vary on;
gzip_min_length 1024;
gzip_types text/plain text/css application/json application/javascript;

# 缓存配置
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m;
proxy_cache my_cache;
```

### 3. 日志管理
```nginx
# 访问日志
access_log /var/log/nginx/tlias_access.log combined;

# 错误日志
error_log /var/log/nginx/tlias_error.log warn;

# 关闭不必要的日志
location /health {
    access_log off;
}
```

## 📝 监控和维护

### 查看 Nginx 状态
```bash
# 查看进程
tasklist | findstr nginx

# 查看端口占用
netstat -ano | findstr :80

# 查看日志
tail -f logs/access.log
tail -f logs/error.log
```

### 常见问题

**1. 端口 80 被占用**
```nginx
# 修改为其他端口
listen 8000;
```

**2. 权限问题**
- Windows: 以管理员身份运行
- Linux: 使用 sudo

**3. 配置不生效**
```bash
# 重新加载配置
nginx -s reload
```

## 🎓 学习建议

1. **当前阶段**：不需要 Nginx，专注于 Spring Boot 开发
2. **进阶学习**：了解 Nginx 基本概念和配置
3. **实战练习**：尝试配置反向代理和负载均衡
4. **生产部署**：学习 HTTPS、安全配置、性能优化

## 📚 参考资源

- Nginx 官方文档: http://nginx.org/en/docs/
- Nginx 配置生成器: https://www.digitalocean.com/community/tools/nginx
- 性能优化指南: https://www.nginx.com/blog/tuning-nginx/

---

**总结**：这个项目目前不需要 Nginx，但了解 Nginx 对未来的生产环境部署很有帮助！
