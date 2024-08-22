package com.easymap.easymap.repository;

import com.easymap.easymap.entity.PedestrianNode;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PedestrianNodeRepository extends JpaRepository<PedestrianNode, Integer> {

    @Query(value = "SELECT * FROM pedestrian_node pn WHERE ST_DWithin(pn.geom, :point, :distance) ORDER BY ST_Distance(pn.geom, :point) ASC LIMIT 1", nativeQuery = true)
    Optional<PedestrianNode> findClosestNodeWithinDistance(@Param("point") Point point, @Param("distance") double distance); // distance 단위: m
}
