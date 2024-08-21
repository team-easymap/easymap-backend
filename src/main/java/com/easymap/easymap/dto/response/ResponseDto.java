package com.easymap.easymap.dto.response;

import com.easymap.easymap.common.ResponseCode;
import com.easymap.easymap.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private String code;
    private String message;

    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto responseBody = new ResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notModified() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_MODIFIED, ResponseMessage.NOT_MODIFIED);
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> authenticationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.AUTHENTICATION_FAILED, ResponseMessage.AUTHENTICATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFound() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.RESOURCE_NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> conflict(String message) {
        ResponseDto responseBody = new ResponseDto(ResponseCode.ETC, ResponseMessage.ETC + ":" + message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> internalServerError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
