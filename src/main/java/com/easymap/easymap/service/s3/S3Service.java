package com.easymap.easymap.service.s3;


import com.amazonaws.HttpMethod;

public interface S3Service {

    String generatePresignedUrl(String fileName, HttpMethod method);
    void deleteImageFromS3(String fileName);
}
