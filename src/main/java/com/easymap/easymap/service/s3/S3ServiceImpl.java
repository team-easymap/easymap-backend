package com.easymap.easymap.service.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

        // 파일 확장자를 기반으로 Content-Type 설정
        String contentType = determineContentType(fileName);
        if (contentType != null) {
            generatePresignedUrlRequest.withContentType(contentType);
        }

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public void deleteImageFromS3(String fileName){
        try {
            amazonS3Client.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error("Failed to delete file {} from S3 bucket: {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    private String determineContentType(String fileName) {
        String extension = getExtension(fileName).toLowerCase();
        switch (extension) {
            case "png":
                return MediaType.IMAGE_PNG_VALUE;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "gif":
                return MediaType.IMAGE_GIF_VALUE;
            case "pdf":
                return MediaType.APPLICATION_PDF_VALUE;
            case "txt":
                return MediaType.TEXT_PLAIN_VALUE;
            case "html":
                return MediaType.TEXT_HTML_VALUE;
            default:
                return null;  // MIME 타입이 정의되지 않은 경우 null 반환
        }
    }
    private String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

}
