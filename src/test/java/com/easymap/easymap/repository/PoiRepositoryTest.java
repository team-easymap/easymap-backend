package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@ActiveProfiles("dev") // 로컬 DB로 테스트 돌리기 위해 프로파일 추가함.

class PoiRepositoryTest {

    @Autowired
    private PoiRepository poiRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DetailedCategoryRepository detailedCategoryRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void 저장테스트(){
        //given
        User user = userRepository.findById(3L).orElseThrow();

        DetailedCategory detailedCategory = detailedCategoryRepository.findById(3L).orElseThrow();

        List<Tag> collect = IntStream.rangeClosed(3, 5).mapToObj(i -> tagRepository.findById(Long.valueOf(i)).orElseThrow())
                .collect(Collectors.toList());

        Poi poi = Poi.builder()
                .user(user)
                .poiAddress("김밥천국")
                .poiLatitude(Double.valueOf(111.1111))
                .poiLongitude(Double.valueOf(22.2222))
                .poiName("우리집")
                .detailedCategory(detailedCategory)
                .tagList(collect)
                .sharable(false)
                .code("0000030")
                .build();


        //when

        Poi save = poiRepository.save(poi);

        log.info(save.toString());

        //then
    }
}