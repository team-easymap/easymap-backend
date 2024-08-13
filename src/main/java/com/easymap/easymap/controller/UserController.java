package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.user.UserNicknameDuplicateRequestDTO;

import com.easymap.easymap.dto.request.user.UserRequiredInfoRequestDto;
import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.user.UserAdditionalInfoResponseDto;
import com.easymap.easymap.dto.response.user.UserNicknameDuplicateResponseDTO;
import com.easymap.easymap.handler.exception.AuthenticationException;
import com.easymap.easymap.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/nickname-check")
    public ResponseEntity<? super UserNicknameDuplicateResponseDTO> userNicknameDuplicateCheck(@RequestBody @Valid UserNicknameDuplicateRequestDTO userNicknameDuplicateDTO){

        boolean rst = userService.userNicknameDuplicateCheck(userNicknameDuplicateDTO);

        return UserNicknameDuplicateResponseDTO.success(rst);

    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/withdraw")
    public ResponseEntity<? super ResponseDto> userWithdraw(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            throw new AuthenticationException("not authenticated");
        }

        userService.userWithdraw(userDetails);


        return ResponseDto.success();
    }

    @GetMapping("additional-info-check")
    public ResponseEntity<? super UserAdditionalInfoResponseDto> userAdditionalInfoCheck(){

        List<String> rst = userService.userAdditionalInfoCheck();

        return UserAdditionalInfoResponseDto.success(rst);
    }

    @PatchMapping("/required")
    public ResponseEntity<ResponseDto> patchUserRequiredInfo(
            @RequestPart("userInfo") UserRequiredInfoRequestDto userInfo,
            @RequestPart(value = "profileImage") MultipartFile profileImage) {

        log.info("required");
        // DTO에 프로필 이미지 설정
        userInfo.setProfileImage(profileImage);

        boolean isChanged = userService.patchUserRequiredInfo(userInfo);
        if (!isChanged) {
            return ResponseDto.notModified();
        }
        return ResponseDto.success();
    }


}
