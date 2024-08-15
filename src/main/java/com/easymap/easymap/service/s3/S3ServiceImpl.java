package com.easymap.easymap.service.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service{

    private final AmazonS3 amazonS3Client;
    private final String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
    }

    public String generatePresignedUrl(String fileName, HttpMethod method) {
        // 1시간 동안 유효한 Presigned URL 생성
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간
        expiration.setTime(expTimeMillis);

        // 요청 메서드를 지정하여 Presigned URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(method)  // GET, PUT, DELETE 등 메서드 지정
                .withExpiration(expiration);

        // 필요시 Content-Type도 설정
        if (method == HttpMethod.PUT) {
            generatePresignedUrlRequest.withContentType("image/png");
        }

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

}
