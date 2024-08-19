package com.easymap.easymap.dto.request.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoutePostRequestDTO {

    private Integer hop;

    private LocalDateTime startTime;

    private List<UserRouteCoordiDTO> data;
}
