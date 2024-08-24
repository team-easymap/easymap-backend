package com.easymap.easymap.dto.process.map.pedestrian;

import com.easymap.easymap.entity.PedestrianLink;
import com.easymap.easymap.entity.PedestrianLinkSlopeStat;
import com.easymap.easymap.entity.PedestrianNode;
import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geomgraph.Edge;

@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedestrianLinkProcessDTO {

    private Long linkId;

    private Geometry geom;

    private PedestrianNodeProcessDTO fromNodeDTO;

    private PedestrianNodeProcessDTO toNodeDTO;


    private Double linkLen;

    private Integer slopeMin;

    private Integer slopeMax;

    private Integer slopeMedian;

    private Integer slopeAvg;

    private Integer slopeCount;

    public PedestrianLinkProcessDTO(PedestrianLink pedestrianLink) {
        PedestrianLinkSlopeStat slopeStat = pedestrianLink.getSlopeStat();

        this.linkId = pedestrianLink.getLinkId();
        this.geom = pedestrianLink.getGeom();
        this.fromNodeDTO = PedestrianNode.mapToDTO(pedestrianLink.getStartNode());
        this.toNodeDTO = PedestrianNode.mapToDTO(pedestrianLink.getEndNode());

        this.linkLen = pedestrianLink.getLength();

        this.slopeMax = slopeStat.getSlopeMax();
        this.slopeMin = slopeStat.getSlopeMin();
        this.slopeMedian = slopeStat.getSlopeMedian();
        this.slopeAvg = slopeStat.getSlopeAvg();
        this.slopeCount = slopeStat.getSlopeCount();


    }


}
