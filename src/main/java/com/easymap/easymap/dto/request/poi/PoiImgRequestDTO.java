package com.easymap.easymap.dto.request.poi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoiImgRequestDTO {

    private String s3Key;
}
