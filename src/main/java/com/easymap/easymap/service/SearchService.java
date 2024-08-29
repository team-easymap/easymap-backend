package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.search.AddressFromCoordinateGetDTO;
import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.search.AddressResultDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.util.coordinate.dto.CoordinatesAndAddress;

public interface SearchService {

    SearchResultResponseDTO searchKeyword(String keyword);


    CoordinatesAndAddress postCoordinateToAddress(SearchAddressPostRequestDTO addressPostRequestDTO);

    AddressResultDTO getAddressFromCoorinate(AddressFromCoordinateGetDTO coordinateGetDTO);
}
