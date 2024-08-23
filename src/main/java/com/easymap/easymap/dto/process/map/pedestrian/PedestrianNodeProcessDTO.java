package com.easymap.easymap.dto.process.map.pedestrian;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedestrianNodeProcessDTO {

    private Integer nodeId;

    private Geometry geom;

    private String legalDistrictCode;

    private String districtName;

    private String subDistrictName;

    private Integer slopeMin;

    private Integer slopeMax;

    private Integer slopeMedian;

    private Integer slopeAvg;

    private Integer slopeCount;

//    public static PedestrianLinkProcessDTO()

}
