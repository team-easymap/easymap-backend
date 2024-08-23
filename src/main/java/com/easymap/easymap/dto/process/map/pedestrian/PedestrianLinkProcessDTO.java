package com.easymap.easymap.dto.process.map.pedestrian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;
import org.locationtech.jts.geomgraph.Edge;

@Getter
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

}
