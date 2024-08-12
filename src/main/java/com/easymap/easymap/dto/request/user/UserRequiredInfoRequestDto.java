package com.easymap.easymap.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserRequiredInfoRequestDto {
    // TODO S3 연결 후 이미지 포함해야함
    private Character gender;
    private LocalDate birthdate;
    private String nickname;
}
