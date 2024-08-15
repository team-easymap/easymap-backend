package com.easymap.easymap.entity;

import com.easymap.easymap.dto.response.poi.PoiImgResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name="poi_img_s3_url")
@Entity
public class PoiImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private Poi poi;

    private String s3Url;

    public static PoiImgResponseDTO mapToDTO(PoiImg poiImg){
        return PoiImgResponseDTO.builder()
                .poiImgId(poiImg.getImgId())
                .poiImgS3Url(poiImg.getS3Url())
                .build();
    }
}
