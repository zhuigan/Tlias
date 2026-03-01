package com.itheima.aspect;

import com.alibaba.fastjson.JSON;
import com.itheima.annotation.OperationLogger;
import com.itheima.mapper.OperationLogMapper;
import com.itheima.pojo.OperationLog;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Around("@annotation(com.itheima.annotation.OperationLogger)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLogger annotation = signature.getMethod().getAnnotation(OperationLogger.class);
        String operation = annotation.value();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        String params = "";
        if (args != null && args.length > 0) {
            try {
                params = JSON.toJSONString(args[0]);
                // 隐藏密码
                if (params.contains("password")) {
                    params = params.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"******\"");
                }
            } catch (Exception e) {
                params = "参数解析失败";
            }
        }

        // 获取用户名
        String username = "匿名用户";
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = JwtUtils.parseJWT(token);
                username = (String) claims.get("username");
            } catch (Exception e) {
                // Token 解析失败，保持匿名
            }
        }

        // 获取IP地址
        String ip = getIpAddress(request);

        // 执行目标方法
        Object result = null;
        String resultStr = "成功";
        try {
            result = joinPoint.proceed();
            
            // 判断结果
            if (result instanceof Result) {
                Result res = (Result) result;
                if (res.getCode() == 0) {
                    resultStr = "失败: " + res.getMsg();
                }
            }
        } catch (Exception e) {
            resultStr = "异常: " + e.getMessage();
            throw e;
        } finally {
            // 记录日志
            OperationLog log = new OperationLog();
            log.setUsername(username);
            log.setOperation(operation);
            log.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
            log.setParams(params);
            log.setIp(ip);
            log.setOperateTime(LocalDateTime.now());
            log.setResult(resultStr);

            try {
                operationLogMapper.insert(log);
                System.out.println("📝 操作日志: " + username + " - " + operation + " - " + resultStr);
            } catch (Exception e) {
                System.err.println("记录操作日志失败: " + e.getMessage());
            }
        }

        return result;
    }

    /**
     * 获取真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
