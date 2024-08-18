package com.easymap.easymap.util.coordinate.dto.vworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VworldCoordinatesDTO {

    @JsonProperty("y")
    private String latitude;

    @JsonProperty("x")
    private String longitude;
}
