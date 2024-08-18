package com.easymap.easymap.service;

import com.easymap.easymap.dto.response.map.MapPoisDTO;

import java.util.List;

public interface MapService {
    List<MapPoisDTO> getPoisOnMap(List<Double> bbox);
}
