package com.easymap.easymap.repository;

import com.easymap.easymap.entity.pedestrian.PedestrianLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedestrianLinkRepository extends JpaRepository<PedestrianLink, Integer> {

    @Query("SELECT pl FROM PedestrianLink pl " +
            "JOIN FETCH pl.endNode en " +  // toNode 엔터티와의 연관관계 패치 조인
            "JOIN FETCH pl.startNode sn " +  // fromNode 엔터티와의 연관관계 패치 조인
            "JOIN FETCH pl.slopeStat ss "+
            "WHERE ST_Within(pl.geom, ST_makeEnvelope(:minX, :minY, :maxX, :maxY, 4326)) = true")
    List<PedestrianLink> findPedestrianLinksInBbox(@Param("minX") double minX,
                                                 @Param("minY") double minY,
                                                 @Param("maxX") double maxX,
                                                 @Param("maxY") double maxY);
}
