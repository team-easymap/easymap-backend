package com.easymap.easymap.util.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WrapperResults {

    @JsonProperty("common")
    private SearchMeta common;

    @JsonProperty("juso")
    private List<AddressData> adsData;

}
