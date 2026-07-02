package com.seagox.lowcode.system.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.OssConfig;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.service.IUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Locale;

/**
 * 文件上传下载
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private IUploadService uploadService;
    
    @Autowired
    private OssConfig ossConfig;

    /**
     * 文件上传
     */
    @LogPoint("文件上传")
    @PostMapping("/putObject/{bucketName}")
    public ResultData putObject(@RequestParam("file") MultipartFile file, @PathVariable String bucketName) throws IOException {
    	String suffix = getSuffix(file);
        String originalFilename = fillFilenameSuffix(file.getOriginalFilename(), suffix);
		String address = uploadService.uploadByInputStream(bucketName, originalFilename,
                file.getContentType(), file.getSize(), file.getInputStream());
        if (StringUtils.isEmpty(address)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "文件服务器配置有误");
        } else {
            return ResultData.success(address);
        }
    }

    private String getSuffix(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.isEmpty(originalFilename) && originalFilename.lastIndexOf(".") > -1) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
        }
        String contentType = file.getContentType();
        if (!StringUtils.isEmpty(contentType) && contentType.startsWith("image/")) {
            String imageType = contentType.substring(contentType.indexOf("/") + 1).toLowerCase(Locale.ROOT);
            return "pjpeg".equals(imageType) ? "jpg" : imageType;
        }
        return "";
    }

    private String fillFilenameSuffix(String originalFilename, String suffix) {
        String filename = StringUtils.isEmpty(originalFilename) ? "upload" : originalFilename;
        if (filename.lastIndexOf(".") > -1) {
            return filename;
        }
        return filename + "." + suffix;
    }

    /**
     * 下载文件
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String url, String fileName) {
        uploadService.download(response, url, fileName);
    }

    /**
     * 下载模版
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, String templateName) {
        uploadService.downloadTemplate(response, templateName);
    }

}
