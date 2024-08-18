package com.easymap.easymap.util.coordinate.dto.vworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VworldCoordinateResponseDTO {

    private String status;

    @JsonProperty("refined")
    private VworldRefinedResultDTO refined;

    @JsonProperty("result")
    private VworldCoordinateResult result;
}
