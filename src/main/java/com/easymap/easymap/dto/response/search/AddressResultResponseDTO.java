package com.easymap.easymap.dto.response.search;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class AddressResultResponseDTO extends ResponseDto {

    private AddressResultDTO data;

    public AddressResultResponseDTO(AddressResultDTO data) {
        this.data = data;
    }

    public static ResponseEntity<ResponseDto> success(AddressResultDTO data) {
        ResponseDto responseBody = new AddressResultResponseDTO(data);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
