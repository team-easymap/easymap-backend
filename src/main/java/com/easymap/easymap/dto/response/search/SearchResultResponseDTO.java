package com.easymap.easymap.dto.response.search;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder
public class SearchResultResponseDTO extends ResponseDto {

    private List<SearchResultPoiResponseDTO> poiResponseDTOList;

    private List<SearchResultAddressResponseDTO> addressResponseDTOList;

    public SearchResultResponseDTO(List<SearchResultPoiResponseDTO> poiResponseDTOList, List<SearchResultAddressResponseDTO> addressResponseDTOList) {
        this.poiResponseDTOList = poiResponseDTOList;
        this.addressResponseDTOList = addressResponseDTOList;
    }

    public static ResponseEntity<ResponseDto> success(SearchResultResponseDTO responseBody) {
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
