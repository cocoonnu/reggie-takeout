package com.cocoon.reggieTakeout.controller;

import com.cocoon.reggieTakeout.common.GlobalResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /** 文件上传功能 **/
    @PostMapping("/upload")
    public GlobalResult<String> upload(MultipartFile file) throws IOException {
        // 如果文件存储的文件夹不存在则新建
        File dir = new File(basePath);
        if (!dir.exists()) dir.mkdirs();

        // 重命名文件
        String originalFilename = file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + suffix;

        // 实现文件上传，文件暂存本地磁盘
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return GlobalResult.success(fileName);
    }

    /** 响应文件上传后的静态资源 **/
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try {
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream =response.getOutputStream();

            int length = 0;
            byte[] bytes = new byte[1024];
            while (length != -1) {
                length = fileInputStream.read(bytes);
                outputStream.write(bytes, 0, length); // 写入
                outputStream.flush(); // 刷新
            }

            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
