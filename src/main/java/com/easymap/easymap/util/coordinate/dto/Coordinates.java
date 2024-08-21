package com.easymap.easymap.util.coordinate.dto;

import com.easymap.easymap.util.coordinate.dto.juso.CoordinateData;
import com.easymap.easymap.util.coordinate.dto.vworld.VworldCoordinateResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    private String status;

    private String address;

    private Double lat;

    private Double lng;

    //private String code; // 법정동 코드

    public static Coordinates mapFromVworldRst(VworldCoordinateResponseDTO vworldCoordinateResponseDTO){
        if(vworldCoordinateResponseDTO.getStatus().equals("NOT_FOUND")){
            return Coordinates.builder()
                    .status("NOT_FOUND")
                    .build();
        }

        return Coordinates.builder()
                .status("FOUND")
                .address(vworldCoordinateResponseDTO.getRefined().getAddressFullText())
                //.code(vworldCoordinateResponseDTO.getRefined().getCode())
                .lat(Double.valueOf(vworldCoordinateResponseDTO.getResult().getPointCoordinates().getLatitude()))
                .lng(Double.valueOf(vworldCoordinateResponseDTO.getResult().getPointCoordinates().getLongitude()))
                .build();
    }


}
