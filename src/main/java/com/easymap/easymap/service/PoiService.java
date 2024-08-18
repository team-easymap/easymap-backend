package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.BboxPoiRequestDTO;
import com.easymap.easymap.dto.request.poi.InstantPoiPostRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.request.review.ReviewPostRequestDTO;
import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import com.easymap.easymap.dto.response.poi.PoiResponseDTO;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import com.easymap.easymap.dto.response.search.SearchResultResponseDTO;

import java.util.List;

public interface PoiService {
    Long addPoi(PoiAddRequestDTO poiAddRequestDTO, String username);

    void updatePoi(Long poiId, PoiUpdateRequestDTO poiUpdateRequestDTO);

    List<CategoryResponseDTO> getCategory();

    PoiResponseDTO findPoiById(Long poiId);

    Long addReview(Long poiId, ReviewPostRequestDTO reviewPostRequestDTO, String username);

    List<ReviewResponseDTO> getReviews(Long poiId);

    Long addInstantPoi(InstantPoiPostRequestDTO instantPoiPostRequestDTO, String username);

    List<PoiResponseDTO> findBboxPoiList(BboxPoiRequestDTO bboxPoiRequestDTO);
}
