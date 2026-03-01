package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
@Mapper
public interface EmpMapper {



    Page<Emp> selectPage(@Param("name") String name,
                         @Param("gender") Short gender,
                         @Param("begin") LocalDate begin,
                         @Param("end") LocalDate end);
    
    Page<Emp> selectPageByDept(@Param("name") String name,
                                @Param("gender") Short gender,
                                @Param("begin") LocalDate begin,
                                @Param("end") LocalDate end,
                                @Param("deptId") Integer deptId);
    
    @Insert("insert into emp (username,name,gender,image,job,entrydate,dept_id,create_time,update_time)" +
            " values (#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime}) ")
    int save(Emp emp);
    //批量删除
    int delete(List<Integer> ids);

    @Select("select id, username, password, name, gender, image, job, entrydate, dept_id, role, create_time, update_time " +
            "from emp " +
            "where id = #{id}")
    Emp findById(Integer id);
    int update(Emp emp);

    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getByUsernameAndPassword(@Param("username") String username,
                                 @Param("password") String password);
    
    @Select("select * from emp where username = #{username}")
    Emp getByUsername(@Param("username") String username);
}