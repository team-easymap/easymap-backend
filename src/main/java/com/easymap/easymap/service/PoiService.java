package com.easymap.easymap.service;

import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;

public interface PoiService {
    Long addPoi(PoiAddRequestDTO poiAddRequestDTO, String username);
}
