package com.easymap.easymap.dto.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteGetRequestDTO {

    @NotNull
    private Long start_poi_id;

    @NotNull
    private Long end_poi_id;

    public Long getStartPoiId() {
        return start_poi_id;
    }

    public Long getEndPoiId() {
        return end_poi_id;
    }
}
