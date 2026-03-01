package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

/**
 * 部门管理
 */
public interface DeptService {
    List<Dept> list();
    Boolean save(Dept dept);
    //根据id删除部门信息
    Boolean deleteById(Integer id);
    Dept getById(Integer id);
    // 修改部门信息
    Boolean update(Dept dept);
}