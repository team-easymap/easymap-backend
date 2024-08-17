package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PoiRepository extends JpaRepository<Poi, Long> {


    Optional<Poi> findPoiByPoiIdAndDeletedAtIsNullAndSharableIsTrue(Long poiId);

    @Query("SELECT p FROM Poi p WHERE " +
            "(p.poiName LIKE CONCAT('%', :keyword, '%') OR p.poiAddress LIKE CONCAT('%', :keyword, '%')) " +
            "AND p.sharable = true " +
            "AND p.deletedAt IS NULL")
    List<Poi> findByKeywordAndSharableAndNotDeleted(@Param("keyword") String keyword);
}
