package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Poi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoiRepository extends JpaRepository<Poi, Long> {


    Optional<Poi> findPoiByPoiIdAndDeletedAtIsNullAndSharableIsTrue(Long poiId);
}
