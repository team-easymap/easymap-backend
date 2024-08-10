package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pois")
@Entity
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poiId;

    @ManyToOne
    private User user;

    private String poiName;

    private String poiAddress;

    @LastModifiedDate
    private LocalDateTime poiRecentUpdateDate;

    private Double poiLatitude;

    private Double poiLongitude;

    @ManyToOne
    private DetailedCategory detailedCategory;

    @OneToMany
    private List<Tag> tagList;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    private String code; //법정동 코드

    private boolean sharable;



}
