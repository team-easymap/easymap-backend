package com.easymap.easymap.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "reviews")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="poiId")
    private Poi poi;

    private Integer score;

    private String reviewText;

    @CreatedDate
    private LocalDateTime createAt;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "review")
    List<ReviewImg> reviewImgList;
}
