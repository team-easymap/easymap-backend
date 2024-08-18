package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.map.MapBboxRequestDTO;
import com.easymap.easymap.dto.response.map.MapPoisDTO;
import com.easymap.easymap.dto.response.map.MapPoisResponseDTO;
import com.easymap.easymap.service.MapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/map")
@RestController
public class MapController {

    private final MapService mapService;

    @GetMapping("/pois")
    public ResponseEntity<?> getPoisOnMap(@RequestParam List<Double> bbox){

        List<MapPoisDTO> mapPoisDTOList = mapService.getPoisOnMap(bbox);

        return MapPoisResponseDTO.success(mapPoisDTOList);
    }
}
