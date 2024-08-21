package com.easymap.easymap.dto.response.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long reviewId;

    private Long userId;

    private String nickname;

    private Long poiId;

    private String poiName;

    private Integer score;

    private String reviewText;

    private LocalDateTime createAt;

    private List<ReviewImgResponseDTO> imgsOnReview;


}
