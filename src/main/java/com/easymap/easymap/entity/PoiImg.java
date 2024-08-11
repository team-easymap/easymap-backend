package com.easymap.easymap.entity;

import jakarta.persistence.*;

@Table(name="poi_img_s3_url")
@Entity
public class PoiImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private Poi poi;

    private String s3Url;
}
