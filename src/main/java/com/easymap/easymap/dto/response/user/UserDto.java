package com.easymap.easymap.dto.response.user;

import com.easymap.easymap.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long userId;
    private String profileS3Key;
    private String email;
    private Character gender;
    private LocalDate birthdate;
    private String nickname;
    private String oauthType;
    private LocalDateTime signupDate;
}
