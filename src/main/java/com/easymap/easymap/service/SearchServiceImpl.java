package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.search.SearchAddressPostRequestDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultAddressResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultPoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.repository.PoiRepository;
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
                .addressResponseDTOList(addressResponseDTOList)
                .poiResponseDTOList(poiResponseDTOList)
                .build();


    }

    @Override
    public PoiResponseDTO postAddressToPoi(SearchAddressPostRequestDTO addressPostRequestDTO) {



        return null;
    }
}
