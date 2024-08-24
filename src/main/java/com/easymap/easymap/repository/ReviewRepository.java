package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeleteAtIsNull(Long id);

    List<Review> findReviewsByPoi_PoiIdAndDeleteAtIsNull(Long poiId);

    List<Review> findReviewsByUser_UserIdAndDeleteAtIsNull(Long userId);

    @Query(value = "UPDATE Review r SET r.deleteAt = now() WHERE r =:review")
    void deleteReviewByReview(@Param("review") Review review);

}
