package com.easymap.easymap.dto.response.coordinate;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.util.coordinate.dto.CoordinatesAndAddress;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CoordinateResponseDTO extends ResponseDto {

    private CoordinatesAndAddress data;

    public CoordinateResponseDTO(CoordinatesAndAddress coordinatesAndAddress) {
        super();
        this.data = coordinatesAndAddress;
    }

    public static ResponseEntity<ResponseDto> success(CoordinatesAndAddress coordinatesAndAddress) {
        ResponseDto responseBody = new CoordinateResponseDTO(coordinatesAndAddress);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
