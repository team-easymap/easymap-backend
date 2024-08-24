package com.easymap.easymap.entity;

import com.easymap.easymap.dto.process.map.pedestrian.PedestrianNodeProcessDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedestrian_node")
@Entity
public class PedestrianNode {
    @Id
    private Integer nodeId;

    @Column(name = "geom")
    private Point geom;

    @Column(name = "bjd_cd")
    private String legalDistrictCode; // 법정동 코드

    @Column(name = "sgg_nm")
    private String districtName; // 시 군 구

    @Column(name = "emd_nm")
    private String subDistrictName; // 읍 면 동

//    @OneToMany(mappedBy = "startNode")
//    private List<PedestrianLink> outgoingLinks;
//
//    @OneToMany(mappedBy = "endNode")
//    private List<PedestrianLink> incomingLinks;

    public static PedestrianNodeProcessDTO mapToDTO(PedestrianNode pedestrianNode) {
        if(pedestrianNode == null) {
            return null;
        }
        return PedestrianNodeProcessDTO.builder()
                .nodeId(pedestrianNode.getNodeId())
                .geom(pedestrianNode.getGeom())
                .legalDistrictCode(pedestrianNode.getLegalDistrictCode())
                .districtName(pedestrianNode.getDistrictName())
                .subDistrictName(pedestrianNode.getSubDistrictName())
                .build();

    }
}
