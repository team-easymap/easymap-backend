package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.search.AddressFromCoordinateGetDTO;
import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.coordinate.CoordinateResponseDTO;
import com.easymap.easymap.dto.response.search.AddressResultDTO;
import com.easymap.easymap.dto.response.search.AddressResultResponseDTO;
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
    public ResponseEntity<?> postCoordinateFromAddress(@RequestBody SearchAddressPostRequestDTO addressPostRequestDTO){

        Coordinates coordinates = searchService.postCoordinateToAddress(addressPostRequestDTO);


        return CoordinateResponseDTO.success(coordinates);
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddressFromCoordinate(AddressFromCoordinateGetDTO coordinateGetDTO){

        AddressResultDTO addressResultDTO = searchService.getAddressFromCoorinate(coordinateGetDTO);

        return AddressResultResponseDTO.success(addressResultDTO);
    }

}
