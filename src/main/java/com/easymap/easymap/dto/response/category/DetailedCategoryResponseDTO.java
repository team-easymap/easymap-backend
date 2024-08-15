package com.easymap.easymap.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedCategoryResponseDTO {

    private Long detailedCategoryId;

    private String detailedCategoryName;
}
