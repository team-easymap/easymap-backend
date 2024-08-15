package com.easymap.easymap.dto.response.poi;

import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import com.easymap.easymap.dto.response.category.DetailedCategoryResponseDTO;
import com.easymap.easymap.dto.response.category.TagResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoiResponseDTO {

    private Long poiId;

    private String poiName;

    private String poiAddress;

    private Long userId;

    private Long poiPointAvg; // 2배수

    private Long poiPointCount;

    private Long poiPointAlly;

    private Double poiLatitude;

    private Double poiLongitude;

    private String categoryName;

    private DetailedCategoryResponseDTO detailedCategoryResponseDTO;

    private List<TagResponseDTO> tagsOnPoi;

    private List<PoiImgResponseDTO> imgsOnPoi;
}
