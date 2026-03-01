package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired //注入mapper
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        //调用持久层mapper，查询部门列表
        List<Dept> depts = deptMapper.findAll();
        return depts;
    }
    @Override
    public Boolean save(Dept dept) {
        //1.补全部门信息
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        //2.调用mapper层的save方法添加部门信息
        int i=deptMapper.save(dept);
        if (i>0){
            return true;
        }
        return false;
    }
    @Override
    public Boolean deleteById(Integer id) {
        //1.调用mapper层的deleteById方法删除部门信息
        int i=deptMapper.deleteById(id);
        if (i>0){
            return true;
        }
        return false;
    }
    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @Override
    public Boolean update(Dept dept) {
        //1.补全修改时间
        dept.setUpdateTime(LocalDateTime.now());
        //2.调用mapper层的update方法修改部门信息
        int i=deptMapper.update(dept);
        //3.返回结果
        if (i>0){
            return true;
        }
        return false;
    }
}