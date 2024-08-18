package com.easymap.easymap.dto.response.poi;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class BboxPoiListResponseDTO extends ResponseDto {

    private List<PoiResponseDTO> pois;

    public BboxPoiListResponseDTO(List<PoiResponseDTO> pois) {
        super();
        this.pois = pois;
    }

    public static ResponseEntity<ResponseDto> success(List<PoiResponseDTO> pois) {
        ResponseDto responseBody = new BboxPoiListResponseDTO(pois);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
