package com.easymap.easymap.dto.response.poi;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class InstantPoiResponseDTO extends ResponseDto {

    private Long poiId;

    public InstantPoiResponseDTO(Long poiId) {
        super();
        this.poiId = poiId;
    }

    public static ResponseEntity<ResponseDto> success(Long poiId) {
        ResponseDto responseBody = new InstantPoiResponseDTO(poiId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
