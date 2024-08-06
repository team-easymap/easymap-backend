package com.easymap.easymap.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class TestResponseDto extends ResponseDto{
    private int testNum;
    private String testMessage;

    public TestResponseDto(int testNum, String testMessage) {
        super();
        this.testNum = testNum;
        this.testMessage = testMessage;
    }

    public static ResponseEntity<ResponseDto> success(int testNum, String testMessage) {
        ResponseDto responseBody = new TestResponseDto(testNum, testMessage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
