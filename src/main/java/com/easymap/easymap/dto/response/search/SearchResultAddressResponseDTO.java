package com.easymap.easymap.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultAddressResponseDTO {

    private String name;

    private String address; // 전체 도로명 주소(roadAddr)

    private String admCd; // 행정 구역 코드

    private String rnMgtSn; // 도로명 코드

    private String udtrYn; // 지하 여부

    private String buldMnnm; // 건물 본번

    private String buldSlno; // 건물 부번
}
