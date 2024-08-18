package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.coordinate.CoordinateResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.service.SearchService;
import com.easymap.easymap.util.coordinate.dto.Coordinates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> searchKeyword(String keyword){
        log.info(keyword);
        SearchResultResponseDTO searched = searchService.searchKeyword(keyword);

        return SearchResultResponseDTO.success(searched);
    }

    @PostMapping("/coordinate")
    public ResponseEntity<?> postAddressToCoordinate(@RequestBody SearchAddressPostRequestDTO addressPostRequestDTO){

        Coordinates coordinates = searchService.postAddressToCoordinate(addressPostRequestDTO);


        return CoordinateResponseDTO.success(coordinates);
    }

}
