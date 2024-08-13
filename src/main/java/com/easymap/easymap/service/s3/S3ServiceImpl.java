package com.easymap.easymap.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.easymap.easymap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ServiceImpl implements S3Service{

    private final AmazonS3 amazonS3Client;
    private final String bucketName = "your-s3-bucket-name"; // S3 버킷 이름을 지정

    @Override
    public String uploadProfileImageToS3(MultipartFile profileImage) {
        String fileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        File file = convertMultipartFileToFile(profileImage);
        String fileUrl = "";

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            fileUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }

        return fileUrl;
    }

    @Override
    public File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipart file to file", e);
        }
        return convertedFile;
    }
}
