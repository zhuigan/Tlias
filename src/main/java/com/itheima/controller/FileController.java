package com.itheima.controller;

import com.itheima.annotation.OperationLogger;
import com.itheima.pojo.Result;
import com.itheima.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * 图片上传到阿里云 OSS
     *
     * @param image
     * @return
     * @throws IOException
     */
    @OperationLogger("上传文件")
    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException {
        System.out.println("=== 文件上传接口被调用 ===");
        System.out.println("文件名: " + (image != null ? image.getOriginalFilename() : "null"));
        System.out.println("文件大小: " + (image != null ? image.getSize() : 0));
        
        if (image == null || image.isEmpty()) {
            System.out.println("错误: 文件为空");
            return Result.error("文件不能为空");
        }
        
        try {
            // 上传到阿里云 OSS
            String url = aliOSSUtils.upload(image);
            System.out.println("上传成功: " + url);
            return Result.success(url);
        } catch (Exception e) {
            System.out.println("上传失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}