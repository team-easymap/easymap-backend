package com.easymap.easymap.controller;

import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final RestTemplate restTemplate;


    @GetMapping("/hello")
    public String hello() {
        return "Hello, World";
    }

    @GetMapping("/test")
    public ResponseEntity<? super TestResponseDto> test() {
        TestResponseDto responseDto = new TestResponseDto(30, "testMessage");
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/connection")
    public ResponseEntity<? super TestResponseDto> connectionTest(){
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://eoollrmedbi4bbf.m.pipedream.net", String.class);
        TestResponseDto responseDto = new TestResponseDto(0, "connectionTest");
        return ResponseEntity.ok(responseDto);
    }

}
