package com.easymap.easymap.entity.poi;

import com.easymap.easymap.dto.response.poi.PoiImgResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="poi_img_s3_url")
@Entity
public class PoiImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private Poi poi;

    private String s3Key;

    public static PoiImgResponseDTO mapToDTO(PoiImg poiImg){
        return PoiImgResponseDTO.builder()
                .imgId(poiImg.getImgId())
                .s3Key(poiImg.getS3Key())
                .build();
    }
}
