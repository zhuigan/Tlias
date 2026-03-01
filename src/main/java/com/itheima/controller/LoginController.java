package com.itheima.controller;

import com.itheima.annotation.OperationLogger;
import com.itheima.pojo.Emp;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private EmpService empService;

    @OperationLogger("用户登录")
    @PostMapping
    public Result login(@RequestBody Emp emp) {
        System.out.println("=== 登录接口被调用 ===");
        System.out.println("用户名: " + emp.getUsername());
        System.out.println("密码: " + emp.getPassword());
        
        try {
            //1. 调用service进行登陆校验
            System.out.println("开始查询用户...");
            Emp empData = empService.getByUsernameAndPassword(emp.getUsername(), emp.getPassword());
            System.out.println("查询结果: " + (empData != null ? empData.getName() : "null"));
            
            if (empData == null) {
                System.out.println("登录失败: 用户名或密码错误");
                return Result.error("用户名或者密码错误");
            }

            //2. 生成jwt token
            System.out.println("开始生成 Token...");
            Map<String, Object> map = new HashMap<>();
            map.put("id", empData.getId());
            map.put("username", empData.getUsername());
            map.put("name", empData.getName());
            map.put("role", empData.getRole());
            map.put("deptId", empData.getDeptId());
            String token = JwtUtils.generateJwt(map);
            
            System.out.println("登录成功: " + empData.getName());
            System.out.println("Token: " + token);

            //3. 返回
            return Result.success(token);
        } catch (Exception e) {
            System.out.println("登录过程出现异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("登录失败: " + e.getMessage());
        }
    }
}