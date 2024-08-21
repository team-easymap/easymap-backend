package com.easymap.easymap.dto.request.search;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAddressPostRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String address; // 전체 도로명 주소(roadAddr)


    private String admCd; // 행정 구역 코드==법정동 코드

    private String rnMgtSn; // 도로명 코드

    private Integer udtrYn; // 지하 여부

    private Integer buldMnnm; // 건물 본번

    private Integer buldSlno; // 건물 부번

}
