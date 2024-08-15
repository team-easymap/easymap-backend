package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.request.review.ReviewPostRequestDTO;
import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.PoiImg;
import com.easymap.easymap.entity.Review;
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
import org.springframework.transaction.annotation.Transactional;
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

    private final CategoryRepository categoryRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;

    private final TagRepository tagRepository;

    private final ReviewRepository reviewRepository;

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
                .sharable(true)
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

    @Transactional
    @Override
    public List<CategoryResponseDTO> getCategory() {

        List<Category> all = categoryRepository.findAll();

        return all.stream().map(Category::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PoiResponseDTO findPoiById(Long poiId) {
        Poi poi = poiRepository.findById(poiId).orElseThrow(() -> new ResourceNotFoundException("no poi : " + poiId));

//        if(!poi.isSharable()){
//            throw new
//        }
        // TODO 삭제된 Poi 처리
//        if (poi.getDeletedAt()!=null){
//            throw new
//        }


        return Poi.mapToDTO(poi);
    }

    @Override
    public Long addReview(Long poiId, ReviewPostRequestDTO reviewPostRequestDTO, String username) {
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no user such as :" + username));

        Poi poi = poiRepository.findPoiByPoiIdAndDeletedAtIsNullAndSharableIsTrue(poiId).orElseThrow(() -> new ResourceNotFoundException("no public poi such as : " + poiId));

        Review review = Review.builder()
                .user(user)
                .poi(poi)
                .score(reviewPostRequestDTO.getScore())
                .reviewText(reviewPostRequestDTO.getReviewText())
                .reviewImgList(null) // TODO Img 로직
                .createAt(LocalDateTime.now())
                .build();

        Review save = reviewRepository.save(review);
        return save.getReviewId();

    }

    @Override
    public List<ReviewResponseDTO> getReviews(Long poiId) {

        List<Review> reviews = reviewRepository.findReviewsByPoi_PoiId(poiId);

        List<ReviewResponseDTO> collect = reviews.stream().map(Review::mapToDTO).collect(Collectors.toList());

        return collect;
    }
}
