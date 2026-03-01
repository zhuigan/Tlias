# 故障排查指南

## 问题：访问 /depts 返回 NOT_LOGIN

### 问题现象
```json
{"code":0,"msg":"NOT_LOGIN"}
```

### 原因分析
拦截器配置可能没有正确排除 `/depts` 路径，或者 Spring Boot 应用需要重启才能生效。

### 解决方案

#### 方案一：重启后端服务（推荐）

1. **停止当前运行的后端服务**
   - 如果使用 IDE：点击停止按钮
   - 如果使用命令行：按 `Ctrl+C`

2. **重新启动后端**
   ```bash
   mvn clean spring-boot:run
   ```
   或在 IDE 中重新运行 `TliasWebManagementApplication`

3. **验证配置是否生效**
   - 访问：http://localhost:8080/depts
   - 应该返回部门列表，而不是 NOT_LOGIN

#### 方案二：检查配置文件

确认 `src/main/java/com/itheima/config/WebConfig.java` 中的配置：

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginCheckInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/login",           // 登录接口
                "/register",        // 注册接口
                "/depts",           // 部门列表（注册页面需要）✅
                "/upload",
                "/uploads/**",
                "/index.html",
                "/register.html",
                "/*.html",
                "/*.css",
                "/*.js",
                "/*.ico",
                "/favicon.ico",
                "/error"
            );
}
```

#### 方案三：临时解决方案（仅用于测试）

如果重启后仍然有问题，可以临时修改 `DeptController`，添加一个不需要认证的接口：

```java
// 在 DeptController 中添加
@GetMapping("/public")
public Result publicList() {
    List<Dept> depts = deptService.list();
    return Result.success(depts);
}
```

然后在前端修改 API 调用：
```javascript
// src/api/dept.js
export const getDeptList = () => {
  return request({
    url: '/depts/public',  // 使用新的公开接口
    method: 'get'
  })
}
```

### 验证步骤

#### 1. 使用浏览器测试
直接访问：http://localhost:8080/depts

**期望结果**：
```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "name": "技术部",
      "createTime": "2024-01-01 00:00:00",
      "updateTime": "2024-01-01 00:00:00"
    }
  ]
}
```

**错误结果**：
```json
{"code":0,"msg":"NOT_LOGIN"}
```

#### 2. 使用 curl 测试
```bash
curl http://localhost:8080/depts
```

#### 3. 使用 Postman 测试
- Method: GET
- URL: http://localhost:8080/depts
- Headers: 不需要添加 token

### 常见问题

#### Q1: 重启后还是返回 NOT_LOGIN
**A**: 检查是否有多个 Spring Boot 实例在运行，关闭所有实例后重新启动。

#### Q2: 其他接口也需要不登录访问怎么办？
**A**: 在 `excludePathPatterns` 中添加对应的路径。

#### Q3: 为什么注册页面需要访问部门列表？
**A**: 注册时用户需要选择所属部门，所以需要获取部门列表。

### 调试技巧

#### 1. 添加日志
在 `LoginCheckInterceptor.preHandle` 方法开头添加：
```java
System.out.println("拦截器执行 - 请求路径: " + request.getRequestURI());
System.out.println("拦截器执行 - Token: " + token);
```

#### 2. 查看控制台输出
启动后端时，应该看到类似输出：
```
拦截器执行 - 请求路径: /depts
拦截器执行 - Token: null
```

如果看到这个输出，说明拦截器配置没有生效。

#### 3. 检查 Spring Boot 版本
确认 `pom.xml` 中的 Spring Boot 版本：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.5</version>
</parent>
```

### 最终解决方案

如果以上方法都不行，可以使用以下方案：

#### 方案 A：修改拦截器逻辑

在 `LoginCheckInterceptor.java` 中添加白名单判断：

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 获取请求路径
    String requestURI = request.getRequestURI();
    
    // 白名单路径，直接放行
    String[] whiteList = {"/login", "/register", "/depts"};
    for (String path : whiteList) {
        if (requestURI.equals(path)) {
            return true;
        }
    }
    
    // 原有的 token 验证逻辑
    String token = request.getHeader("token");
    if (token == null || token.isEmpty()) {
        Result notLogin = Result.error("NOT_LOGIN");
        String resultJson = JSON.toJSONString(notLogin);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(resultJson);
        return false;
    }
    
    try {
        Claims claims = JwtUtils.parseJWT(token);
    } catch (Exception e) {
        Result notLogin = Result.error("NOT_LOGIN");
        String resultJson = JSON.toJSONString(notLogin);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(resultJson);
        return false;
    }
    
    return true;
}
```

#### 方案 B：使用注解方式

1. 创建自定义注解：
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAuth {
}
```

2. 在 DeptController 的 list 方法上添加注解：
```java
@NoAuth
@GetMapping
public Result list() {
    List<Dept> depts = deptService.list();
    return Result.success(depts);
}
```

3. 在拦截器中检查注解：
```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (handler instanceof HandlerMethod) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
        if (noAuth != null) {
            return true; // 有 @NoAuth 注解，直接放行
        }
    }
    // 原有的验证逻辑...
}
```

### 推荐方案

**最简单有效的方案**：

1. 确保 `WebConfig.java` 中 `/depts` 在排除列表中
2. 完全停止后端服务
3. 清理编译缓存：`mvn clean`
4. 重新启动：`mvn spring-boot:run`
5. 测试：访问 http://localhost:8080/depts

如果还是不行，使用**方案 A**（在拦截器中添加白名单判断），这是最可靠的方法。

### 联系支持

如果问题仍然存在，请提供以下信息：
1. Spring Boot 版本
2. 控制台完整日志
3. 浏览器 Network 面板截图
4. WebConfig.java 完整代码
