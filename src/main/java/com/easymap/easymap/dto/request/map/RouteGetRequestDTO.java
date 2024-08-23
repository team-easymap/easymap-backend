package com.easymap.easymap.dto.request.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RouteGetRequestDTO {

    private Long startPoiId;

    private Long endPoiId;
}
