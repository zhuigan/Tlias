package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 查询数据列表 select * from emp
        Page<Emp> empPage = empMapper.selectPage(name, gender, begin, end);

        //3. 封装返回
        return new PageBean(empPage.getTotal(), empPage.getResult());
    }
    
    @Override
    public PageBean pageByDept(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end, Integer deptId) {
        //1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        //2. 查询同部门数据列表
        Page<Emp> empPage = empMapper.selectPageByDept(name, gender, begin, end, deptId);

        //3. 封装返回
        return new PageBean(empPage.getTotal(), empPage.getResult());
    }
    
    @Override
    public Boolean save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        int i=empMapper.save(emp);
        if(i>0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean delete(List<Integer> ids) {
        //1.调用mapper层的delete方法删除员工信息
        int i=empMapper.delete(ids);
        if (i>0){
            return true;
        }
        return false;
    }
    @Override
    public Emp getById(Integer id) {
        return empMapper.findById(id);
    }

    @Override
    public Boolean update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        int i=empMapper.update(emp);
        if (i>0){
            return true;
        }
        return false;
    }
    @Override
    public Emp getByUsernameAndPassword(String username, String password) {
        return empMapper.getByUsernameAndPassword(username, password);
    }
    
    @Override
    public Emp getByUsername(String username) {
        return empMapper.getByUsername(username);
    }
}