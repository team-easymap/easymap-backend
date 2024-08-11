package com.easymap.easymap.entity;

import jakarta.persistence.*;

@Table(name = "review_img_s3_url")
@Entity
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private String s3Url;
}
