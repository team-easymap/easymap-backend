package com.easymap.easymap.dto.request.poi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BboxPoiRequestDTO {

    private Long categoryId;

    private List<Double> bbox;
}
