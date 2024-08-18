package com.easymap.easymap.dto.response.map;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class MapPoisResponseDTO extends ResponseDto {

    private List<MapPoisDTO> data;

    public MapPoisResponseDTO(List<MapPoisDTO> data) {
        super();
        this.data = data;
    }

    public static ResponseEntity<ResponseDto> success(List<MapPoisDTO> data) {
        ResponseDto responseBody = new MapPoisResponseDTO(data);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
