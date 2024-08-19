package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.map.UserRoutePostRequestDTO;
import com.easymap.easymap.dto.response.map.MapPoisDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MapService {
    List<MapPoisDTO> getPoisOnMap(List<Double> bbox);

    Long postUserRoute(UserRoutePostRequestDTO userRoutePostRequestDTO, UserDetails userDetails);
}
