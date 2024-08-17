package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.service.SearchService;
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

    @PostMapping("/address")
    public ResponseEntity<?> postAddressToPoi(@RequestBody SearchAddressPostRequestDTO addressPostRequestDTO){

        PoiResponseDTO poiResponseDTO = searchService.postAddressToPoi(addressPostRequestDTO);


        return null;
    }

}
