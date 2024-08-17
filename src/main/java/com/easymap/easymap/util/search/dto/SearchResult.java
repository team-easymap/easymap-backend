package com.easymap.easymap.util.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResult {

    @JsonProperty("results")
    private WrapperResults results;

}

