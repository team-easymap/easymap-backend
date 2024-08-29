package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.map.MapBboxRequestDTO;
import com.easymap.easymap.dto.request.map.RouteGetRequestDTO;
import com.easymap.easymap.dto.request.map.UserRoutePostRequestDTO;
import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.map.MapPoisDTO;
import com.easymap.easymap.dto.response.map.MapPoisResponseDTO;
import com.easymap.easymap.dto.response.map.RouteDTO;
import com.easymap.easymap.dto.response.map.RouteResponseDTO;
import com.easymap.easymap.service.MapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/map")
@RestController
public class MapController {

    private final MapService mapService;

    @GetMapping("/pois")
    public ResponseEntity<? super MapPoisResponseDTO> getPoisOnMap(@RequestParam List<Double> bbox){

        List<MapPoisDTO> mapPoisDTOList = mapService.getPoisOnMap(bbox);

        return MapPoisResponseDTO.success(mapPoisDTOList);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/routes")
    public ResponseEntity<? super ResponseDto> postUserRoute(@RequestBody UserRoutePostRequestDTO userRoutePostRequestDTO, @AuthenticationPrincipal UserDetails userDetails){

        Long userRouteId = mapService.postUserRoute(userRoutePostRequestDTO, userDetails);

        return ResponseDto.success();
    }

    @GetMapping("/routes")
    public ResponseEntity<? super RouteResponseDTO> getRoute(@ModelAttribute @Valid RouteGetRequestDTO requestDTO){
        log.info(requestDTO.toString());

        List<RouteDTO> routeBetweenPois = mapService.getRouteBetweenPois(requestDTO);


        return RouteResponseDTO.success(routeBetweenPois);
    }
}
