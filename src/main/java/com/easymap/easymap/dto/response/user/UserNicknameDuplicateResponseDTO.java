package com.easymap.easymap.dto.response.user;

import com.easymap.easymap.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class UserNicknameDuplicateResponseDTO extends ResponseDto {

    private boolean duplicate;

    public UserNicknameDuplicateResponseDTO(boolean duplicate) {
        super();
        this.duplicate = duplicate;
    }

    public static ResponseEntity<ResponseDto> success(boolean duplicate) {
        ResponseDto responseBody = new UserNicknameDuplicateResponseDTO(duplicate);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
