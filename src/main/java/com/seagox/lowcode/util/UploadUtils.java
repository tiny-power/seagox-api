package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.minio.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadUtils {

	/**
     * minio上传
     */
    public static String minioOss(String endpoint, String accessKey, String secretKey, String url, String bucketName,
                                  String originalFilename, InputStream inputStream, String contentType) {
    	try {
            MinioClient minioClient = MinioClient.builder().endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                JSONObject config = new JSONObject();
                config.put("Version", "2012-10-17");
                JSONArray statements = new JSONArray();

                JSONObject statement = new JSONObject();
                JSONArray action = new JSONArray();
                action.add("s3:PutObject");
                action.add("s3:AbortMultipartUpload");
                action.add("s3:DeleteObject");
                action.add("s3:GetObject");
                action.add("s3:ListMultipartUploadParts");
                statement.put("Action", action);
                statement.put("Effect", "Allow");
                statement.put("Principal", "*");
                statement.put("Resource", "arn:aws:s3:::"
                        + bucketName
                        + "/*");
                statements.add(statement);
                config.put("Statement", statements);
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(config.toJSONString()).build());
            }
            String temporary = String.valueOf(System.currentTimeMillis());
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = DigestUtils.md5Hex(temporary) + "." + suffix;
            Map<String, String> headers = new HashMap<>();
            headers.put("x-amz-acl", "public-read-write");
            
            Date date = new Date( );
		    SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMM");
		    String yearMonth = sdf.format(date);	
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(yearMonth + "/" + fileName).stream(inputStream, inputStream.available(), -1).contentType(contentType).headers(headers).build());
            return url  + "/" + bucketName + "/" + yearMonth + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

	/**
	 * 阿里云oss上传
	 */
	public static String aliyunOss(String endpoint, String accessKey, String secretKey, String url, String bucketName,
			String originalFilename, InputStream inputStream, String contentType) {
		OSSClient ossClient = new OSSClient(endpoint, accessKey, secretKey);
		try {
			boolean isExist = ossClient.doesBucketExist(bucketName);
			if (!isExist) {
				CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
				createBucketRequest.setCannedACL(CannedAccessControlList.PublicReadWrite);
				ossClient.createBucket(createBucketRequest);
			}
			String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 创建上传Object的Metadata
			ObjectMetadata meta = new ObjectMetadata();
			// 设置上传的内容类型
			meta.setContentType(contentType);
			String temporary = String.valueOf(System.currentTimeMillis());
			String fileName = DigestUtils.md5Hex(temporary) + "." + suffix;
			// 上传文件流。
			ossClient.putObject(bucketName, fileName, inputStream, meta);
			if (endpoint.startsWith("https")) {
				return "https://" + bucketName + "." + url.substring(8, url.length()) + "/" + fileName;
			} else {
				return "https://" + bucketName + "." + url.substring(7, url.length()) + "/" + fileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 关闭OSSClient。
			ossClient.shutdown();
		}
	}

	/**
	 * 移动云oss上传
	 */
	public static String ecloudOss(String endpoint, String accessKey, String secretKey, String url, String bucketName,
			String originalFilename, InputStream inputStream, String contentType, Long size) {
		try {
			ClientConfiguration opts = new ClientConfiguration();
			opts.setSignerOverride("S3SignerType");
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3 s3 = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, null))
					.withClientConfiguration(opts).build();
			boolean isExist = s3.doesBucketExistV2(bucketName);
			if (!isExist) {
				com.amazonaws.services.s3.model.CreateBucketRequest createBucketRequest = new com.amazonaws.services.s3.model.CreateBucketRequest(
						bucketName);
				createBucketRequest
						.setCannedAcl(com.amazonaws.services.s3.model.CannedAccessControlList.PublicReadWrite);
				s3.createBucket(createBucketRequest);
			}
			String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			com.amazonaws.services.s3.model.ObjectMetadata meta = new com.amazonaws.services.s3.model.ObjectMetadata();
			meta.setContentType(contentType);
			meta.setContentLength(size);
			meta.setHeader("x-amz-acl", com.amazonaws.services.s3.model.CannedAccessControlList.PublicReadWrite);
			String temporary = String.valueOf(System.currentTimeMillis());
			String fileName = DigestUtils.md5Hex(temporary) + "." + suffix;
			s3.putObject(bucketName, fileName, inputStream, meta);
			return url + "/" + bucketName + "/" + fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
