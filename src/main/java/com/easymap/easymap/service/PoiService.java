package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;

public interface PoiService {
    Long addPoi(PoiAddRequestDTO poiAddRequestDTO, String username);

    void updatePoi(Long poiId, PoiUpdateRequestDTO poiUpdateRequestDTO);
}
