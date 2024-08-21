package com.easymap.easymap.dto.response.coordinate;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import com.easymap.easymap.util.coordinate.dto.Coordinates;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CoordinateResponseDTO extends ResponseDto {

    private Coordinates data;

    public CoordinateResponseDTO(Coordinates coordinates) {
        super();
        this.data = coordinates;
    }

    public static ResponseEntity<ResponseDto> success(Coordinates coordinates) {
        ResponseDto responseBody = new CoordinateResponseDTO(coordinates);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
