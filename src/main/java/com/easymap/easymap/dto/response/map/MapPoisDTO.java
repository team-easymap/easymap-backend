package com.easymap.easymap.dto.response.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapPoisDTO {

    private Long poiId;

    private String poiName;

    private String type;

    private Double lat;

    private Double lng;

}
