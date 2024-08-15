package com.easymap.easymap.dto.request.poi;

import com.easymap.easymap.dto.request.category.TagRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoiUpdateRequestDTO {

    @NotBlank
    @JsonProperty("poi_name")
    private String poiName;


    @JsonProperty("poi_address")
    private String poiAddress;

    @NotNull
    @JsonProperty("detailed_category_id")
    private Long detailedCategoryId;

//    @NotNull
//    @Min(-90)
//    @Max(90)
//    @JsonProperty("lat")
//    private Double poiLatitude;
//
//    @NotNull
//    @Min(-180)
//    @Max(180)
//    @JsonProperty("lng")
//    private Double poiLongitude;
//
//    @NotNull
//    @JsonProperty("poi_code")
//    private String code;

    @JsonProperty("tag_list")
    private List<TagRequestDTO> tagList;

    private List<MultipartFile> fileList;

}
