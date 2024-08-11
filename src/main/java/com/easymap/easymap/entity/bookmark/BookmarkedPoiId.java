package com.easymap.easymap.entity.bookmark;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BookmarkedPoiId implements Serializable {

    private Long userId;
    private Long poiId;

    @Override
    public int hashCode() {
        return Objects.hash(userId, poiId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj==null || getClass() != obj.getClass()) return false;

        BookmarkedPoiId that = (BookmarkedPoiId) obj;

        return Objects.equals(userId, that.getPoiId()) && Objects.equals(poiId, that.getPoiId());

    }
}
