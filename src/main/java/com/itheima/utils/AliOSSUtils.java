package com.itheima.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */
@Component
public class AliOSSUtils {

    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId = "aliyun.oss.accessKeyId";
    private String accessKeySecret = "aliyun.oss.accessKeySecret";
    private String bucketName = "hmleadnews1111111111111111111111111";

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        System.out.println("=== 开始上传到阿里云 OSS ===");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("BucketName: " + bucketName);
        
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println("生成的文件名: " + fileName);

        try {
            //上传文件到 OSS
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            System.out.println("OSS 客户端创建成功");
            
            ossClient.putObject(bucketName, fileName, inputStream);
            System.out.println("文件上传到 OSS 成功");

            //文件访问路径
            String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
            System.out.println("文件访问 URL: " + url);
            
            // 关闭ossClient
            ossClient.shutdown();
            return url;// 把上传到oss的路径返回
        } catch (Exception e) {
            System.out.println("上传到 OSS 失败: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("上传到阿里云 OSS 失败: " + e.getMessage());
        }
    }

}