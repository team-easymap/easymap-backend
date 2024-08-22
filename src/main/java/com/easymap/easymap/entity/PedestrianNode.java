package com.easymap.easymap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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
}
