package com.itheima.controller;

import com.itheima.annotation.OperationLogger;
import com.itheima.pojo.Emp;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private EmpService empService;

    @OperationLogger("用户注册")
    @PostMapping
    public Result register(@RequestBody Emp emp) {
        System.out.println("=== 注册接口被调用 ===");
        System.out.println("用户名: " + emp.getUsername());
        System.out.println("姓名: " + emp.getName());
        
        try {
            // 1. 验证用户名是否已存在
            Emp existingEmp = empService.getByUsername(emp.getUsername());
            if (existingEmp != null) {
                System.out.println("注册失败: 用户名已存在");
                return Result.error("用户名已存在，请更换");
            }
            
            // 2. 设置默认值
            if (emp.getRole() == null) {
                emp.setRole((short) 2); // 默认为普通用户
            }
            
            if (emp.getEntrydate() == null) {
                emp.setEntrydate(LocalDate.now()); // 默认入职日期为今天
            }
            
            // 3. 保存到数据库
            Boolean success = empService.save(emp);
            
            if (success) {
                System.out.println("注册成功: " + emp.getName());
                return Result.success("注册成功！请登录");
            } else {
                System.out.println("注册失败: 数据库保存失败");
                return Result.error("注册失败，请稍后重试");
            }
            
        } catch (Exception e) {
            System.out.println("注册过程出现异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("注册失败: " + e.getMessage());
        }
    }
}
