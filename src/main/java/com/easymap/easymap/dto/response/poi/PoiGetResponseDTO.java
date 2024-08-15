package com.easymap.easymap.dto.response.poi;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PoiGetResponseDTO extends ResponseDto {

    private PoiResponseDTO poiResponseDTO;

    public PoiGetResponseDTO(PoiResponseDTO poiResponseDTO) {
        super();
        this.poiResponseDTO = poiResponseDTO;
    }

    public static ResponseEntity<ResponseDto> success(PoiResponseDTO poiResponseDTO) {
        ResponseDto responseBody = new PoiGetResponseDTO(poiResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
