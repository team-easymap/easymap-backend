package com.easymap.easymap.dto.request.poi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstantPoiPostRequestDTO {

    private String type;

    @JsonProperty("place")
    private PlaceRequestDTO place;

}
