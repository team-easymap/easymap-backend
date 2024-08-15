package com.easymap.easymap.entity;

import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.response.category.DetailedCategoryResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pois")
@Entity
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poiId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String poiName;

    private String poiAddress;

    @LastModifiedDate
    private LocalDateTime poiRecentUpdateDate;

    private Double poiLatitude;

    private Double poiLongitude;

    @ManyToOne
    @JoinColumn(name = "detailedCategory_id")
    private DetailedCategory detailedCategory;

    @OneToMany
    private List<Tag> tagList;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    private String code; //법정동 코드

    private boolean sharable;

    @OneToMany(mappedBy = "poi")
    private List<PoiImg> poiImgList;

    @OneToMany(mappedBy = "poi")
    private List<Review> reviewList;


    public void update(PoiUpdateRequestDTO poiUpdateRequestDTO, DetailedCategory detailedCategory, List<Tag> tagList, List<PoiImg> poiImgList) {
        this.poiName = poiUpdateRequestDTO.getPoiName();
        this.poiAddress = poiUpdateRequestDTO.getPoiAddress();
        this.detailedCategory = detailedCategory;
        this.tagList = tagList;
        this.poiImgList = poiImgList;


    }

    public static PoiResponseDTO mapToDTO(Poi poi){
        return PoiResponseDTO.builder()
                .poiId(poi.getPoiId())
                .poiName(poi.getPoiName())
                .poiAddress(poi.getPoiAddress())
                .userId(poi.user.getUserId())
                .poiLatitude(poi.getPoiLatitude())
                .poiLongitude(poi.getPoiLongitude())
                .detailedCategoryResponseDTO(DetailedCategory.mapToDTO(poi.getDetailedCategory()))
                .categoryName(poi.getDetailedCategory().getCategory().getCategoryName())
                .tagsOnPoi(poi.getTagList().stream().map(t-> Tag.mapToDTO(t)).collect(Collectors.toList()))
                .imgsOnPoi(poi.getPoiImgList().stream().map(img-> PoiImg.mapToDTO(img)).collect(Collectors.toList()))
                .poiPointAlly(poi.getTagList().stream().mapToLong(t->t.getTagAccessibilityPoint()).sum())
                .poiPointCount(poi.getReviewList().stream().count())
                .poiPointAvg(poi.getReviewList().stream().mapToLong(review->review.getScore()).sum())
                .build();
    }
}
