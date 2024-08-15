package com.easymap.easymap.entity;

import com.easymap.easymap.dto.response.review.ReviewImgResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "review_img_s3_url")
@Entity
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String s3Url;

    public static ReviewImgResponseDTO mapToDTO(ReviewImg reviewImg){
        return ReviewImgResponseDTO.builder()
                .imgId(reviewImg.getImgId())
                .s3Url(reviewImg.getS3Url())
                .build();
    }
}
