package com.easymap.easymap.controller;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@AllArgsConstructor
@Slf4j
public class TestController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello, World";
    }

    @GetMapping("/test")
    public ResponseEntity<? super TestResponseDto> test() {
        TestResponseDto responseDto = new TestResponseDto(30, "testMessage");
        return ResponseEntity.ok(responseDto);
    }

}
