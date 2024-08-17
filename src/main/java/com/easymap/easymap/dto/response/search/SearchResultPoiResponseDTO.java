package com.easymap.easymap.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultPoiResponseDTO {

    private Long poiId;

    private String poiName;

    private String poiAddress;

    private Double lat;

    private Double lng;

}
