package com.easymap.easymap.dto.response.map;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class RouteResponseDTO extends ResponseDto {

    private List<RouteDTO> routes;

    public RouteResponseDTO(List<RouteDTO> routes) {
        this.routes = routes;
    }

    public static ResponseEntity<ResponseDto> success(List<RouteDTO> routes) {
        ResponseDto responseBody = new RouteResponseDTO(routes);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
