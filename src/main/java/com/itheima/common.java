package com.itheima;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ControllerAdvice 这个注解表示当前类是一个全局异常处理器 可以处理项目中所有的异常
// annotations属性设置你要捕获的异常范围  {RestController.class, Controller.class}表示要捕获的异常范围是
// RestController注解或者Controller注解修饰的类
@ResponseBody // 这个注解表示当前方法的返回值应该直接写给浏览器，并且返回的是json格式的数据
@Slf4j // 这个注解是由 lombok提供的，表示在项目编译的时候给当前类生成日志对象
public class common {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    // @ExceptionHandler 这个注解表示当前方法可以处理项目中的异常
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("捕获到异常：{}",ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }
}