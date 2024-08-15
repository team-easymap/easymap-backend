package com.easymap.easymap.dto.response.s3;

import com.easymap.easymap.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
public class S3PresignedUrlResponseDto extends ResponseDto {

    private String presignedUrl;

    public S3PresignedUrlResponseDto(String presignedUrl){
        super();
        this.presignedUrl = presignedUrl;
    }

    public static ResponseEntity<ResponseDto> success(String presignedUrl) {
        ResponseDto responseBody = new S3PresignedUrlResponseDto(presignedUrl);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
