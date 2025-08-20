package com.seagox.lowcode.auth.serivce.impl;

import com.seagox.lowcode.auth.serivce.IUploadService;
import com.seagox.lowcode.common.OssConfig;
import com.seagox.lowcode.util.DocumentConverterUtils;
import com.seagox.lowcode.util.UploadUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Service
public class UploadService implements IUploadService {

	@Autowired
    private OssConfig ossConfig;

    @Autowired
    private DocumentConverterUtils documentConverterUtils;

    @Override
    public String uploadByInputStream(String bucketName, String originalFilename, String contentType, long size, InputStream inputStream) {
        String address = null;
        if (ossConfig.getType().equals("minio")) {
            address = UploadUtils.minioOss(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getSecretKey(), ossConfig.getUrl(), bucketName, originalFilename, inputStream, contentType);
        } else if (ossConfig.getType().equals("aliyun")) {
            address = UploadUtils.aliyunOss(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getSecretKey(), ossConfig.getUrl(), bucketName, originalFilename, inputStream, contentType);
        } else if (ossConfig.getType().equals("ecloud")) {
            address = UploadUtils.ecloudOss(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getSecretKey(), ossConfig.getUrl(), bucketName, originalFilename, inputStream, contentType, size);
        }
        return address;
    }

    @Override
    public void download(HttpServletResponse response, String url, String fileName) {
        url = url.replace(ossConfig.getUrl(), ossConfig.getEndpoint());
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            inputStream = new URL(url).openStream();
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void downloadTemplate(HttpServletResponse response, String templateName) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((templateName).getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Resource resource = new ClassPathResource("\\template\\" + templateName);
            inputStream = resource.getInputStream();
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void preview(String url, String fileName, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String prefix = fileName.substring(0, fileName.lastIndexOf("."));
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(prefix + ".pdf", "UTF-8"));
            response.setContentType("application/pdf");
            outputStream = response.getOutputStream();
            inputStream = new URL(url).openStream();
            documentConverterUtils.convert(suffix, inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
