package com.easymap.easymap.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AddressResultDTO {

    private Integer zipcode;

    private String parcel;

    private String road;
}
