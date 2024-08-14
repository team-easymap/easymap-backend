package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.PoiImg;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PoiServiceImpl implements PoiService{

    private final PoiRepository poiRepository;

    private final UserRepository userRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;

    private final TagRepository tagRepository;

    @Override
    public Long addPoi(PoiAddRequestDTO poiAddRequestDTO, String username) {

        Optional<User> userByEmailAndDeactivationDateIsNull = userRepository.findUserByEmailAndDeactivationDateIsNull(username);
        User user = userByEmailAndDeactivationDateIsNull.orElseThrow(() -> new ResourceNotFoundException("no such user : " + username));

        DetailedCategory detailedCategory = detailedCategoryRepository.findById(poiAddRequestDTO.getDetailedCategoryId()).orElseThrow(() -> new ResourceNotFoundException("no such detailed category"));

        List<Tag> tagList = poiAddRequestDTO.getTagList().stream().map(tag -> tagRepository.findById(tag.getTagId()).orElseThrow(() -> new ResourceNotFoundException("no such tag")))
                .collect(Collectors.toList());

        List<PoiImg> poiImgList = null;

        Poi poi = Poi.builder()
                .user(user)
                .detailedCategory(detailedCategory)
                .poiName(poiAddRequestDTO.getPoiName())
                .poiAddress(poiAddRequestDTO.getPoiAddress())
                .poiLatitude(poiAddRequestDTO.getPoiLatitude())
                .poiLongitude(poiAddRequestDTO.getPoiLongitude())
                .poiRecentUpdateDate(LocalDateTime.now())
                .code(poiAddRequestDTO.getCode()) // 일단 프론트에서 받아오는 걸로
                .sharable(false)
                .tagList(tagList)
                .poiImgList(poiImgList)
                .build();

        // TODO 파일 처리 로직 구현
        
        Poi save = poiRepository.save(poi);

        return save.getPoiId();
    }

    @Override
    public void updatePoi(Long poiId, PoiUpdateRequestDTO poiUpdateRequestDTO) {
        DetailedCategory detailedCategory = detailedCategoryRepository.findById(poiUpdateRequestDTO.getDetailedCategoryId()).orElseThrow(() -> new ResourceNotFoundException("no such detailed category"));

        List<Tag> tagList = poiUpdateRequestDTO.getTagList().stream().map(tag -> tagRepository.findById(tag.getTagId()).orElseThrow(() -> new ResourceNotFoundException("no such tag")))
                .collect(Collectors.toList());

        // TODO poiImgList 나중에 추가 구현
        List<PoiImg> poiImgList = null;

        Poi poi = poiRepository.findById(poiId).orElseThrow(()-> new ResourceNotFoundException("no such Poi :"+ poiId));


        poi.update(poiUpdateRequestDTO, detailedCategory, tagList, poiImgList);

        poiRepository.save(poi);

    }
}
