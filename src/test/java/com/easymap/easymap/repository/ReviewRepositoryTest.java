package com.easymap.easymap.repository;

import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.Review;
import com.easymap.easymap.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PoiRepository poiRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void 리뷰레포등록테스트(){
        //given
        Poi poi = poiRepository.findById(1L).get();
        User user = userRepository.findById(1L).get();


        Review review = Review.builder()
                .poi(poi)
                .score(10)
                .reviewText("리뷰 등록 테스트")
                .reviewImgList(null)
                .user(user)
                .build();
        //when
        reviewRepository.save(review);

        //then
        //log.info(save.getReviewText().toString());


    }
}