package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 部门管理
 */
@Mapper
public interface DeptMapper {

    @Select("select * from dept")
    List<Dept> findAll();
    @Insert("INSERT INTO dept (NAME, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime})")
    int save(Dept dept);
    //根据id删除部门信息
    @Delete("delete from dept where id=#{id}")
    int deleteById(Integer id);
    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept getById(Integer id);
    //修改部门信息
    @Update("update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    int update(Dept dept);
}