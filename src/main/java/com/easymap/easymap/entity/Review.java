package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "reviews")
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="poi_id")
    private Poi poi;

    private Integer score;

    private String reviewText;

    @CreatedDate
    private LocalDateTime createAt;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "review")
    List<ReviewImg> reviewImgList;
}
