package com.easymap.easymap.controller;

import com.easymap.easymap.common.ResponseCode;
import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;
import com.easymap.easymap.dto.response.ResponseDto;

import com.easymap.easymap.dto.response.user.UserNicknameDuplicateResponseDTO;
import com.easymap.easymap.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("nickname-check")
    public ResponseEntity<? super UserNicknameDuplicateResponseDTO> userNicknameDuplicateCheck(@RequestBody @Valid UserNicknameDuplicateRequestDTO userNicknameDuplicateDTO){

        //log.info(nicknameDuplicateDTO.getNickname());
//        if(bindingResult.hasFieldErrors()){
//            throw new ;
//        }
        boolean rst = userService.userNicknameDuplicateCheck(userNicknameDuplicateDTO);

        return UserNicknameDuplicateResponseDTO.success(rst);



    }


}
