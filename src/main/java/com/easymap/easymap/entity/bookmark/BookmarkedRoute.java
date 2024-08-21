package com.easymap.easymap.entity.bookmark;

import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "bookmarked_routes")
@Entity
public class BookmarkedRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkedId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="start_poi_id")
    private Poi startPoi;

    @ManyToOne
    @JoinColumn(name="end_poi_id")
    private Poi endPoi;


    private LocalDateTime deleteAt;

    @CreatedDate
    private LocalDateTime createdAt;

    private Long routeId;

}
