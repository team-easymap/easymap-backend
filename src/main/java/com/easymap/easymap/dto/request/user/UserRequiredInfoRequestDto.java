package com.easymap.easymap.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserRequiredInfoRequestDto {

    private Character gender;
    private LocalDate birthdate;
    private String nickname;
    private MultipartFile profileImage;
}
