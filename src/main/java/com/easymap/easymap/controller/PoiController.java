package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.poi.BboxPoiRequestDTO;
import com.easymap.easymap.dto.request.poi.InstantPoiPostRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.request.review.ReviewPostRequestDTO;
import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.category.CategoryGetResponseDTO;
import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import com.easymap.easymap.dto.response.poi.BboxPoiListResponseDTO;
import com.easymap.easymap.dto.response.poi.InstantPoiResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiGetResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.review.ReviewGetResponseDTO;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;
import com.easymap.easymap.handler.exception.AuthenticationException;
import com.easymap.easymap.service.PoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/poi")
@RestController
public class PoiController {

    private final PoiService poiService;

    @GetMapping("/{poiId}")
    public ResponseEntity<?> getPoi(@PathVariable("poiId") Long poiId){
        log.info("get Access for " + poiId);

        PoiResponseDTO poi = poiService.findPoiById(poiId);
        return PoiGetResponseDTO.success(poi);
    }

    @GetMapping("/categorys")
    public ResponseEntity<?> getCategory(){
        log.info("get Category");
        List<CategoryResponseDTO> categoryResponseDTOList= poiService.getCategory();

        return CategoryGetResponseDTO.success(categoryResponseDTOList);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value="/add")
    public ResponseEntity<?> addPoi(@RequestBody @Valid PoiAddRequestDTO poiAddRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            throw new AuthenticationException("not authenticated");
        }
        log.info(poiAddRequestDTO.toString());

        Long poiId= poiService.addPoi(poiAddRequestDTO, userDetails.getUsername());

        return ResponseDto.success();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value="/{poiId}/update")
    public ResponseEntity<?> updatePoi(@PathVariable(value = "poiId") Long poiId, @RequestBody @Valid PoiUpdateRequestDTO poiUpdateRequestDTO){

        log.info(poiUpdateRequestDTO.toString());

        poiService.updatePoi(poiId, poiUpdateRequestDTO);

        return ResponseDto.success();

    }

    // 일단 Review 관련 로직도 여기 구현
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{poiId}/review")
    public ResponseEntity<?> postReview(@PathVariable(value = "poiId") Long poiId, @Valid @RequestBody ReviewPostRequestDTO reviewPostRequestDTO, @AuthenticationPrincipal UserDetails userDetails){

        Long reviewId = poiService.addReview(poiId, reviewPostRequestDTO, userDetails.getUsername());

        return ResponseDto.success();
    }

    @GetMapping(value = "/{poiId}/review")
    public ResponseEntity<?> getReviews(@PathVariable(value = "poiId")Long poiId){

        List<ReviewResponseDTO> reviewList = poiService.getReviews(poiId);

        return ReviewGetResponseDTO.success(reviewList);

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/instantpoi")
    public ResponseEntity<?> postInstantPoi(@RequestBody InstantPoiPostRequestDTO instantPoiPostRequestDTO, @AuthenticationPrincipal UserDetails userDetails){

        Long poiId = poiService.addInstantPoi(instantPoiPostRequestDTO, userDetails.getUsername());

        return InstantPoiResponseDTO.success(poiId);
    }

    @PostMapping("/list")
    public ResponseEntity<?> postBboxPoiList(@RequestBody @Valid BboxPoiRequestDTO bboxPoiRequestDTO){

        List<PoiResponseDTO> poiResponseDTOList = poiService.findBboxPoiList(bboxPoiRequestDTO);
        return BboxPoiListResponseDTO.success(poiResponseDTOList);
    }


}
