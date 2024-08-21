package com.easymap.easymap.dto.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewImgResponseDTO {

    private Long imgId;

    private String s3Key;
}
