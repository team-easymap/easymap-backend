package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.BboxPoiRequestDTO;
import com.easymap.easymap.dto.request.poi.InstantPoiPostRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.request.review.ReviewPostRequestDTO;
import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultAddressResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultPoiResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.entity.*;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import com.easymap.easymap.handler.exception.ResourceNotFoundException;
import com.easymap.easymap.repository.*;
import com.easymap.easymap.service.s3.S3Service;
import com.easymap.easymap.service.s3.S3ServiceImpl;
import com.easymap.easymap.util.search.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private final SearchUtil searchUtil;

    private final S3Service s3Service;

    @Transactional
    @Override
    public Long addPoi(PoiAddRequestDTO poiAddRequestDTO, String username) {

        Optional<User> userByEmailAndDeactivationDateIsNull = userRepository.findUserByEmailAndDeactivationDateIsNull(username);
        User user = userByEmailAndDeactivationDateIsNull.orElseThrow(() -> new ResourceNotFoundException("no such user : " + username));

        DetailedCategory detailedCategory = detailedCategoryRepository.findById(poiAddRequestDTO.getDetailedCategoryId()).orElseThrow(() -> new ResourceNotFoundException("no such detailed category"));

        List<Tag> tagList = poiAddRequestDTO.getTagList().stream().map(tag -> tagRepository.findById(tag.getTagId()).orElseThrow(() -> new ResourceNotFoundException("no such tag")))
                .collect(Collectors.toList());



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
                .build();

        // 빌더에서 참조관계 설정 안되어서 poi 생성 후 세터 주입
        List<PoiImg> poiImgs = poiAddRequestDTO.getImages().stream().map(s3key -> PoiImg.builder().poi(poi).s3Key(s3key.getS3Key()).build()).collect(Collectors.toList());
        poi.setPoiImgList(poiImgs);

        
        Poi save = poiRepository.save(poi);

        return save.getPoiId();
    }

    @Transactional
    @Override
    public void updatePoi(Long poiId, PoiUpdateRequestDTO poiUpdateRequestDTO) {
        DetailedCategory detailedCategory = detailedCategoryRepository.findById(poiUpdateRequestDTO.getDetailedCategoryId()).orElseThrow(() -> new ResourceNotFoundException("no such detailed category"));

        List<Tag> tagList = poiUpdateRequestDTO.getTagList().stream().map(tag -> tagRepository.findById(tag.getTagId()).orElseThrow(() -> new ResourceNotFoundException("no such tag")))
                .collect(Collectors.toList());


        Poi poi = poiRepository.findById(poiId).orElseThrow(()-> new ResourceNotFoundException("no such Poi :"+ poiId));

        List<PoiImg> newPoiImgList = poiUpdateRequestDTO.getImages().stream().map(s3key-> PoiImg.builder().poi(poi).s3Key(s3key.getS3Key()).build()).collect(Collectors.toList());
        // 업데이트 한 이미지 중 겹치는것 제외하고 삭제
        poi.getPoiImgList().stream().filter(img-> newPoiImgList.stream().map(newImg-> newImg.getS3Key()).collect(Collectors.toList()).contains(img.getS3Key()))
                        .forEach(img-> s3Service.deleteImageFromS3(img.getS3Key()));

        poi.update(poiUpdateRequestDTO, detailedCategory, tagList, newPoiImgList);

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
                .createAt(LocalDateTime.now())
                .build();

        List<ReviewImg> reviewImgs = reviewPostRequestDTO.getImages().stream().map(dto -> ReviewImg.builder().review(review).s3Key(dto.getS3Key()).build()).collect(Collectors.toList());
        review.setReviewImgList(reviewImgs);

        Review save = reviewRepository.save(review);
        return save.getReviewId();

    }

    @Override
    public List<ReviewResponseDTO> getReviews(Long poiId) {

        List<Review> reviews = reviewRepository.findReviewsByPoi_PoiId(poiId);

        List<ReviewResponseDTO> collect = reviews.stream().map(Review::mapToDTO).collect(Collectors.toList());

        return collect;
    }

    @Override
    public Long addInstantPoi(InstantPoiPostRequestDTO instantPoiPostRequestDTO, String username) {

        // 동일 좌표에 공개 POI가 있는 경우 해당 POI ID를 리턴
        List<Poi> pois = poiRepository.findByPoiLatitudeAndPoiLongitude(instantPoiPostRequestDTO.getPlace().getLatitude(), instantPoiPostRequestDTO.getPlace().getLongitude(), PageRequest.of(0, 1));
        if(pois.size()!=0){
            return pois.get(0).getPoiId();
        }
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(username).orElseThrow(() -> new ResourceNotFoundException("no user such as : " + username));

        Poi newInstantPoi = Poi.builder()
                .poiAddress(instantPoiPostRequestDTO.getPlace().getAddress())
                .poiLatitude(instantPoiPostRequestDTO.getPlace().getLatitude())
                .poiLongitude(instantPoiPostRequestDTO.getPlace().getLongitude())
                .sharable(false)
                .poiRecentUpdateDate(LocalDateTime.now())
                .user(user)
                .build();

        Poi save = poiRepository.save(newInstantPoi);


        return save.getPoiId();
    }

    @Transactional
    @Override
    public List<PoiResponseDTO> findBboxPoiList(BboxPoiRequestDTO bboxPoiRequestDTO) {
        // lt_lat, lt_lng, rb_lat, rb_lng
        List<Double> bbox = bboxPoiRequestDTO.getBbox();

        bbox.forEach(i-> log.info(i.toString()));

        Double smLat = Math.min(bbox.get(0), bbox.get(2));
        Double bLat = Math.max(bbox.get(0), bbox.get(2));
        Double smLng = Math.min(bbox.get(1), bbox.get(3));
        Double bLng = Math.max(bbox.get(1), bbox.get(3));



        List<Poi> poisInBbox = poiRepository.findPoiInBbox(bboxPoiRequestDTO.getCategoryId(), smLat, bLat, smLng, bLng);

        List<PoiResponseDTO> collect = poisInBbox.stream().map(poi -> Poi.mapToDTO(poi)).collect(Collectors.toList());

        return collect;
    }


}
