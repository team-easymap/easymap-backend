package com.easymap.easymap.dto.response.user;

import com.easymap.easymap.common.ResponseCode;
import com.easymap.easymap.common.ResponseMessage;
import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.entity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadUserStatusResponseDto extends ResponseDto {

    private UserDto user;

    public LoadUserStatusResponseDto(User user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.user = User.mapToDTO(user);
    }

    public static ResponseEntity<ResponseDto> success(User user) {
        ResponseDto responseBody = new LoadUserStatusResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> userDeactivated() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.USER_DEACTIVATED, ResponseMessage.USER_DEACTIVATED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

}
