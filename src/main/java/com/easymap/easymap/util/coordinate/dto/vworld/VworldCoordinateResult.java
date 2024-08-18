package com.easymap.easymap.util.coordinate.dto.vworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VworldCoordinateResult {

    @JsonProperty("crs")
    private String coordinateSystem;

    @JsonProperty("point")
    private VworldCoordinatesDTO pointCoordinates;
}
