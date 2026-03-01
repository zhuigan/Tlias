package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
public interface EmpService {
    PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end);
    
    PageBean pageByDept(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end, Integer deptId);

    Boolean save(Emp emp);

    Boolean delete(List<Integer> ids);

    Emp getById(Integer id);

    Boolean update(Emp emp);

    Emp getByUsernameAndPassword(String username, String password);
    
    Emp getByUsername(String username);
}
