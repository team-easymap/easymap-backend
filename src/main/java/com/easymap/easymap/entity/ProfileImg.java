package com.easymap.easymap.entity;

import jakarta.persistence.*;

@Table(name = "profile_img_s3_url")
@Entity
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String s3Url;
}
