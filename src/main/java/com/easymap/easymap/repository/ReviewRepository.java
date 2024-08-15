package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByPoi_PoiId(Long poiId);

}
