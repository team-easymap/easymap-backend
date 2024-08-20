package com.easymap.easymap.util.coordinate.dto.vworld;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class VworldAddressDTO {

    private Integer zipcode;

    private String type;

    private String text;

}
