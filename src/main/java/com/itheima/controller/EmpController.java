package com.itheima.controller;

import com.itheima.annotation.OperationLogger;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理Controller
 */
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @GetMapping
    public Result page(
            String name,
            Short gender,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            Integer page,
            Integer pageSize,
            HttpServletRequest request) {
        
        // 从token中获取用户信息
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer role = (Integer) claims.get("role");
        Integer deptId = (Integer) claims.get("deptId");
        
        // 如果是普通用户，只能查看同部门员工
        if (role != null && role == 2 && deptId != null) {
            PageBean pageBean = empService.pageByDept(page, pageSize, name, gender, begin, end, deptId);
            return Result.success(pageBean);
        }
        
        // 管理员可以查看所有员工
        PageBean pageBean = empService.page(page, pageSize, name, gender, begin, end);
        return Result.success(pageBean);
    }
    
    @GetMapping("/current")
    public Result getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer id = (Integer) claims.get("id");
        
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }
    
    @OperationLogger("添加员工")
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        //1.调用业务逻辑层，保存员工信息
        Boolean flag=empService.save(emp);
        if (flag) {
            return Result.success();
        }

        return Result.error("添加员工信息失败");
    }
    
    @OperationLogger("删除员工")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids")List<Integer> ids){
        //1.调用业务逻辑层删除员工信息
        Boolean flag= empService.delete(ids);
        //2.返回结果
        if (flag){
            return Result.success();
        }
        return Result.error("删除员工信息失败");

    }
    
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }
    
    @OperationLogger("修改员工信息")
    @PutMapping
    public Result update(@RequestBody Emp emp){
        Boolean flag=empService.update(emp);
        if(flag){
            return Result.success();
        }
        return Result.error("");
    }
    
    @OperationLogger("更新头像")
    @PutMapping("/avatar")
    public Result updateAvatar(@RequestBody Emp emp, HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(token);
        Integer currentUserId = (Integer) claims.get("id");
        
        // 只能更新自己的头像
        if (!currentUserId.equals(emp.getId())) {
            return Result.error("只能更新自己的头像");
        }
        
        Boolean flag = empService.update(emp);
        if(flag){
            return Result.success();
        }
        return Result.error("更新头像失败");
    }
}