package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.search.AddressFromCoordinateGetDTO;
import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.search.AddressResultDTO;
import com.easymap.easymap.dto.response.search.SearchResultAddressResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultPoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.repository.PoiRepository;
import com.easymap.easymap.util.coordinate.CoordinateConverterUtil;
import com.easymap.easymap.util.coordinate.dto.CoordinatesAndAddress;
import com.easymap.easymap.util.search.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService{

    private final SearchUtil searchUtil;

    private final CoordinateConverterUtil coordinateConverterUtil;

    private final PoiRepository poiRepository;


    @Override
    public SearchResultResponseDTO searchKeyword(String keyword) {
        // 향후 비동기로 수정
        List<SearchResultAddressResponseDTO> addressResponseDTOList = searchUtil.searchKeyword(keyword).stream()
                .map(adsDate -> SearchResultAddressResponseDTO.builder()
                        .name(adsDate.getBdNm())
                        .address(adsDate.getRoadAddr())
                        .admCd(adsDate.getAdmCd())
                        .rnMgtSn(adsDate.getRnMgtSn())
                        .udtrYn(adsDate.getUdrtYn())
                        .buldMnnm(adsDate.getBuldMnnm())
                        .buldSlno(adsDate.getBuldSlno())
                        .build()).limit(10L).collect(Collectors.toList());

        // DB 결과 조회
        List<SearchResultPoiResponseDTO> poiResponseDTOList = poiRepository.findByKeywordAndSharableAndNotDeleted(keyword).stream().map(poi -> SearchResultPoiResponseDTO.builder()
                .poiId(poi.getPoiId())
                .poiName(poi.getPoiName())
                .poiAddress(poi.getPoiAddress())
                .lat(poi.getPoiLatitude())
                .lng(poi.getPoiLongitude())
                .build()).limit(20L).collect(Collectors.toList());

        return SearchResultResponseDTO.builder()
                .adsData(addressResponseDTOList)
                .poiData(poiResponseDTOList)
                .build();


    }

    @Override
    public CoordinatesAndAddress postCoordinateToAddress(SearchAddressPostRequestDTO addressPostRequestDTO) {

        // 일단 vworld로 구현
        CoordinatesAndAddress coordinates = coordinateConverterUtil.convertByAddressFromVworld(addressPostRequestDTO);


        return coordinates;
    }

    @Override
    public AddressResultDTO getAddressFromCoorinate(AddressFromCoordinateGetDTO coordinateGetDTO) {

        Double latitude = coordinateGetDTO.getLatitude();
        Double longitude = coordinateGetDTO.getLongitude();

        AddressResultDTO addressResultDTO = coordinateConverterUtil.convertCoordinateIntoAddress(latitude, longitude);

        return addressResultDTO;
    }
}
