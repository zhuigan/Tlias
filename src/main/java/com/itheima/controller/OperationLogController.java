package com.itheima.controller;

import com.itheima.mapper.OperationLogMapper;
import com.itheima.pojo.OperationLog;
import com.itheima.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/logs")
public class OperationLogController {

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 查询最近的操作日志
     */
    @GetMapping("/recent")
    public Result getRecentLogs(@RequestParam(defaultValue = "50") Integer limit) {
        List<OperationLog> logs = operationLogMapper.selectRecent(limit);
        return Result.success(logs);
    }

    /**
     * 查询所有操作日志
     */
    @GetMapping
    public Result getAllLogs() {
        List<OperationLog> logs = operationLogMapper.selectAll();
        return Result.success(logs);
    }
}
