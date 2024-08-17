package com.easymap.easymap.dto.request.user;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoadUserStatusRequestDto {

    @Nullable
    private Long userId;

}
