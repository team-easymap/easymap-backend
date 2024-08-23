package com.easymap.easymap.dto.process.map.pedestrian;

import jakarta.persistence.Column;
import lombok.*;
import org.geolatte.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedestrianNodeProcessDTO {

    private Integer nodeId;

    private Point geom;

    private String legalDistrictCode;

    private String districtName;

    private String subDistrictName;


}
