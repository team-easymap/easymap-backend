package com.easymap.easymap.controller;

import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiUpdateRequestDTO;
import com.easymap.easymap.dto.response.ResponseDto;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/poi")
@RestController
public class PoiController {

    private final PoiService poiService;



    @PreAuthorize("isAuthenticated()")
    @PostMapping(value="/add", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
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

}
