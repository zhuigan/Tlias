package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理Controller
 */
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired //注入Service
    private DeptService deptService;

    @GetMapping
    public Result list() {
        //调用服务层方法进行查询
        List<Dept> depts = deptService.list(); //list()方法可能会报错，后续在Service中实现即可。快捷在接口中定义 alt+Enter
        return Result.success(depts);
    }
    @PostMapping
    public Result save(@RequestBody Dept dept){
        //1.调用 业务逻辑层添加部门信息
        Boolean flag = deptService.save(dept);
        //2.返回结果
        if (flag){
            return Result.success();
        }
        return Result.error("添加部门信息失败");
    }
    /**
     * 根据id删除部门信息
     * @param id
     * @return
     * @PathVariable 表示获取请求路径中的占位符参数
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        //1.调用业务逻辑层删除部门信息
        Boolean flag= deptService.deleteById(id);
        //2.返回结果
        if (flag){
            return Result.success();
        }
        return Result.error("删除部门信息失败");

    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id) {
        //调用service查询部门
        Dept dept = deptService.getById(id);
        //响应部门数据
        return Result.success(dept);
    }
    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Dept dept){
        //1.调用业务逻辑层修改部门信息
        Boolean flag= deptService.update(dept);
        //2.返回结果
        if (flag){
            return Result.success();
        }
        return Result.error("修改部门信息失败");

    }
}