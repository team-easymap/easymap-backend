package com.easymap.easymap.dto.request.map;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapBboxRequestDTO {

    @Size(min=4, max=4, message = "bbox 리스트는 4개의 요소를 요구함.")
    private List<Double> bbox;
}
