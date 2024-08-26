package com.easymap.easymap.entity.bookmark;

import com.easymap.easymap.entity.poi.Poi;
import com.easymap.easymap.entity.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class BookmarkedPoi {

    @EmbeddedId
    private BookmarkedPoiId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("poiId")
    @JoinColumn(name = "poi_id")
    private Poi poi;

    @CreatedDate
    private LocalDateTime createAt;
}
