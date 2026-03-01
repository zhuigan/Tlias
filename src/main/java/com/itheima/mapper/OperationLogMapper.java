package com.itheima.mapper;

import com.itheima.pojo.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作日志Mapper
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 插入操作日志
     */
    @Insert("INSERT INTO operation_log (username, operation, method, params, ip, operate_time, result) " +
            "VALUES (#{username}, #{operation}, #{method}, #{params}, #{ip}, #{operateTime}, #{result})")
    void insert(OperationLog log);
    
    /**
     * 查询最近的操作日志
     */
    @Select("SELECT * FROM operation_log ORDER BY operate_time DESC LIMIT #{limit}")
    List<OperationLog> selectRecent(Integer limit);
    
    /**
     * 查询所有操作日志
     */
    @Select("SELECT * FROM operation_log ORDER BY operate_time DESC")
    List<OperationLog> selectAll();
}
