package com.easymap.easymap.dto.process.map.pedestrian;

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

    private String bjdCd;

    private String sggNm;

    private String endNm;

    private Double linkLen;

    private Integer slopeMin;

    private Integer slopeMax;

    private Integer slopeMedian;

    private Integer slopeAvg;

    private Integer slopeCount;


}
