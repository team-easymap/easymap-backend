package com.easymap.easymap.repository;

import com.easymap.easymap.entity.pedestrian.PedestrianNode;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedestrianNodeRepository extends JpaRepository<PedestrianNode, Integer> {

    @Query(value = "SELECT * FROM pedestrian_node pn WHERE ST_DWithin(pn.geom, :point, :distance) ORDER BY ST_Distance(pn.geom, :point) ASC LIMIT 1", nativeQuery = true)
    Optional<PedestrianNode> findClosestNodeWithinDistance(@Param("point") Point point, @Param("distance") double distance); // distance 단위: m

    @Query(value = "SELECT * FROM pedestrian_node pn WHERE ST_Within(pn.geom, ST_makeEnvelope(:minX, :minY, :maxX, :maxY, 4326))", nativeQuery = true)
    List<PedestrianNode> findPedestrianNodesInBbox(Double minX, Double minY, Double maxX, Double maxY);
}
