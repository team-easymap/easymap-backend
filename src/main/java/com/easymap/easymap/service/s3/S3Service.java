package com.easymap.easymap.service.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3Service {

    String uploadProfileImageToS3(MultipartFile profileImage);
    File convertMultipartFileToFile(MultipartFile file);
}
