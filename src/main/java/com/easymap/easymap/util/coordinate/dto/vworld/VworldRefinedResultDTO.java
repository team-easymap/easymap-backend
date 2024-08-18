package com.easymap.easymap.util.coordinate.dto.vworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VworldRefinedResultDTO {

    @JsonProperty("text")
    private String addressFullText;

    @JsonProperty("level4AC")
    private String code;


}
