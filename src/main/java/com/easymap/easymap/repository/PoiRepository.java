package com.easymap.easymap.repository;

import com.easymap.easymap.entity.poi.Poi;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT p FROM Poi p WHERE "+
            "p.poiLatitude = :lat AND p.poiLongitude = :lng")
    List<Poi> findByPoiLatitudeAndPoiLongitude(@Param("lat") Double poiLatitude, @Param("lng") Double poiLongitude, Pageable pageable);


    @Query("SELECT p FROM Poi p "
            + "JOIN p.detailedCategory dc "
            + "WHERE (:categoryId IS NULL OR dc.category.categoryId = :categoryId) " +
            "AND p.poiLatitude BETWEEN :smLat AND :bLat " +
            "AND p.poiLongitude BETWEEN :smLng AND :bLng")
    List<Poi> findPoiInBbox(@Param("categoryId") Long categoryId,
                            @Param("smLat") double smLat,
                            @Param("bLat") double bLat,
                            @Param("smLng") double smLng,
                            @Param("bLng") double bLng,
                            Pageable pageable);
}
