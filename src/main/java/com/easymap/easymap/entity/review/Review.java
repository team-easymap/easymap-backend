package com.easymap.easymap.entity.review;

import com.easymap.easymap.dto.request.review.ReviewUpdateRequestDTO;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.entity.user.User;
import com.easymap.easymap.entity.poi.Poi;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="poi_id")
    private Poi poi;

    private Integer score;

    private String reviewText;

    @CreatedDate
    private LocalDateTime createAt;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReviewImg> reviewImgList;

    public static ReviewResponseDTO mapToDTO(Review review){
        return ReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .nickname(review.getUser().getNickname())
                .poiId(review.getPoi().getPoiId())
                .poiName(review.getPoi().getPoiName())
                .score(review.getScore())
                .reviewText(review.getReviewText())
                .createAt(review.getCreateAt())
                .imgsOnReview(review.getReviewImgList().stream().map(img-> ReviewImg.mapToDTO(img)).collect(Collectors.toList()))
                .build();
    }

    public void update(ReviewUpdateRequestDTO reviewUpdateRequestDTO, List<ReviewImg> imgList) {
        this.score = reviewUpdateRequestDTO.getScore();
        this.reviewText = reviewUpdateRequestDTO.getReviewText();
        this.reviewImgList.clear();
        this.reviewImgList.addAll(imgList);


    }

    public void setReviewImgList(List<ReviewImg> reviewImgList){
        this.reviewImgList = reviewImgList;
    }
}
