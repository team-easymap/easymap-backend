package com.easymap.easymap.controller;

import com.easymap.easymap.config.SecurityConfig;
import com.easymap.easymap.dto.request.category.TagRequestDTO;
import com.easymap.easymap.dto.request.poi.PoiAddRequestDTO;
import com.easymap.easymap.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = PoiController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
class PoiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username="testuser", roles={"USER"})
    public void testAddPoi() throws Exception{

        //given
        PoiAddRequestDTO requestDTO = PoiAddRequestDTO.builder()
                .poiName("Test POI")
                .poiAddress("도선동 일번지")
                .detailedCategoryId(3L)
                .poiLatitude(37.7749)
                .poiLongitude(-122.4194)
                .tagList(List.of(new TagRequestDTO(1L), new TagRequestDTO(2L)))
                .build();

        log.info(objectMapper.writeValueAsString(requestDTO));
        // when
        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))

                //then
                .andExpect(status().isOk());

    }

}