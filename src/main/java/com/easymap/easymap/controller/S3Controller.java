package com.easymap.easymap.controller;

import com.amazonaws.HttpMethod;
import com.easymap.easymap.dto.response.s3.S3PresignedUrlResponseDto;
import com.easymap.easymap.service.s3.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/s3")
@RestController
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/generate-presigned-url/put")
    public ResponseEntity<? super S3PresignedUrlResponseDto> generatePresignedUrl(
            @RequestParam("fileName") String fileName) {
        String presignedUrl = s3Service.generatePresignedUrl(fileName, HttpMethod.PUT);
        log.info(presignedUrl);
        return S3PresignedUrlResponseDto.success(presignedUrl);
    }
}
