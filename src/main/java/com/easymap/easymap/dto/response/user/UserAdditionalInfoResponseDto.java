package com.easymap.easymap.dto.response.user;

import com.easymap.easymap.dto.response.ResponseDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class UserAdditionalInfoResponseDto extends ResponseDto {

    List<String> requireList;

    public UserAdditionalInfoResponseDto(List<String> requireList){
        super();
        this.requireList = requireList;
    }

    public static ResponseEntity<ResponseDto> success(List<String> requireList) {
        ResponseDto responseBody = new UserAdditionalInfoResponseDto(requireList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
