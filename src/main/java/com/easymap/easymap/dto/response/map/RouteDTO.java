package com.easymap.easymap.dto.response.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RouteDTO {

    private String type;

    private Double distance;

    private Double slope;

    private LocalDateTime timeRequired;

    private List<RouteNodeDTO> list;
}
