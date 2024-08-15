package com.easymap.easymap.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UserRequiredInfoRequestDto {

    private Character gender;
    private LocalDate birthdate;
    private String nickname;
    private String profileS3Key;
}
