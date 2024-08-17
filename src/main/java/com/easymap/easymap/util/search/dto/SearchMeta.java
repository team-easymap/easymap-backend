package com.easymap.easymap.util.search.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchMeta {

    private String countPerPage;
    private String totalCount;
    private String currentPage;
    private String errorMessage;
    private String errorCode;
}
