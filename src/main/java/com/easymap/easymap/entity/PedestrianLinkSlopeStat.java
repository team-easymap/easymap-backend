package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedestrian_link_slope_stat")
@Entity
public class PedestrianLinkSlopeStat {

    @Id
    private Integer linkId;

    private Integer slopeMin;

    private Integer slopeMax;

    private Integer slopeMedian;

    private Integer slopeSum;

    private Integer slopeCount;

    private Integer slopeAvg;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "link_id")
    private PedestrianLink pedestrianLink;
}
