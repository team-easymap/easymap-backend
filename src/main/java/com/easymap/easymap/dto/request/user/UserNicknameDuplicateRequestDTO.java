package com.easymap.easymap.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserNicknameDuplicateRequestDTO {

    @NotBlank
    private String nickname;
}
