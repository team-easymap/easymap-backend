package com.easymap.easymap.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagResponseDTO {

    private Long tagId;

    private String tagName;

    private Integer tagAccessibilityPoint;

}
