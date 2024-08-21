package com.easymap.easymap.dto.response.search;

import com.easymap.easymap.dto.response.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder
public class SearchResultResponseDTO extends ResponseDto {

    private List<SearchResultPoiResponseDTO> poiData;

    private List<SearchResultAddressResponseDTO> adsData;

    public SearchResultResponseDTO(List<SearchResultPoiResponseDTO> poiData, List<SearchResultAddressResponseDTO> adsData) {
        this.poiData = poiData;
        this.adsData = adsData;
    }

    public static ResponseEntity<ResponseDto> success(SearchResultResponseDTO responseBody) {
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
