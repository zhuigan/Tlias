package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {
    private Integer id;
    private String username;        // 操作用户
    private String operation;       // 操作类型（登录、注册、上传头像等）
    private String method;          // 请求方法
    private String params;          // 请求参数
    private String ip;              // IP地址
    private LocalDateTime operateTime; // 操作时间
    private String result;          // 操作结果
}
