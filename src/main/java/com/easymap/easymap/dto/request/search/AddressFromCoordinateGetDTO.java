package com.easymap.easymap.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AddressFromCoordinateGetDTO {

    private Double latitude;

    private Double longitude;
}
