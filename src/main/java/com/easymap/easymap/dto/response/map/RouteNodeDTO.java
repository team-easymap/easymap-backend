package com.easymap.easymap.dto.response.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RouteNodeDTO {

    private Double lat;

    private Double lng;
}
