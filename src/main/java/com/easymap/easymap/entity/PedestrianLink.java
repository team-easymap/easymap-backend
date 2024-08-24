package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedestrian_link")
@Entity
public class PedestrianLink {
    @Id
    private Long linkId;

    @Column(name = "geom")
    private Geometry geom;

    @Column(name = "link_len")
    private double length;

    @ManyToOne
    @JoinColumn(name = "from_node_id", insertable = false, updatable = false)
    private PedestrianNode startNode;

    @ManyToOne
    @JoinColumn(name = "to_node_id",insertable = false, updatable = false)
    private PedestrianNode endNode;

    @OneToOne(mappedBy = "pedestrianLink", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PedestrianLinkSlopeStat slopeStat;
}
