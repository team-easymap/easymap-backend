package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByPoi_PoiId(Long poiId);

    List<Review> findReviewsByUser_UserId(Long userId);

    @Query(value = "UPDATE Review r SET r.deleteAt = now() WHERE r =:review")
    void deleteReviewByReview(@Param("review") Review review);

}
