package com.easymap.easymap.service;

import com.easymap.easymap.dto.response.map.MapPoisDTO;
import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.repository.CategoryRepository;
import com.easymap.easymap.repository.DetailedCategoryRepository;
import com.easymap.easymap.repository.PoiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapServiceImpl implements MapService{

    private final PoiRepository poiRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;



    @Transactional(readOnly = true)
    @Override
    public List<MapPoisDTO> getPoisOnMap(List<Double> bbox) {
        Double smLat = Math.min(bbox.get(0), bbox.get(2));
        Double bLat = Math.max(bbox.get(0), bbox.get(2));
        Double smLng = Math.min(bbox.get(1), bbox.get(3));
        Double bLng = Math.max(bbox.get(1), bbox.get(3));

        List<Poi> poisInBbox = poiRepository.findPoiInBbox(null, smLat, bLat, smLng, bLng);

        Category build = Category.builder().categoryId(4L).build();

        List<Long> obstacleDcIdList = detailedCategoryRepository.findDetailedCategoryByCategory(build).stream().map(dc -> dc.getDetailedCategoryId()).collect(Collectors.toList());

        List<MapPoisDTO> collect = poisInBbox.stream().map(poi -> MapPoisDTO.builder()
                        .poiId(poi.getPoiId())
                        .poiName(poi.getPoiName())
                        .type(obstacleDcIdList.contains(poi.getPoiId()) ? "obstacle" : "place")
                        .lat(poi.getPoiLatitude())
                        .lng(poi.getPoiLongitude())
                        .build())
                .collect(Collectors.toList());


        return collect;
    }
}
