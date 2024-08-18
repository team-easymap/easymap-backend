package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.util.coordinate.dto.Coordinates;

public interface SearchService {

    SearchResultResponseDTO searchKeyword(String keyword);


    Coordinates postAddressToCoordinate(SearchAddressPostRequestDTO addressPostRequestDTO);
}
