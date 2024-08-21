package com.easymap.easymap.entity;

import com.easymap.easymap.dto.response.review.ReviewImgResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review_img_s3_url")
@Entity
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String s3Key;

    public static ReviewImgResponseDTO mapToDTO(ReviewImg reviewImg){
        return ReviewImgResponseDTO.builder()
                .imgId(reviewImg.getImgId())
                .s3Key(reviewImg.getS3Key())
                .build();
    }
}
