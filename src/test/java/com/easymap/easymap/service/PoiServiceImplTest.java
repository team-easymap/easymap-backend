package com.easymap.easymap.service;

import com.easymap.easymap.entity.Poi;
import com.easymap.easymap.entity.User;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import com.easymap.easymap.repository.DetailedCategoryRepository;
import com.easymap.easymap.repository.TagRepository;
import com.easymap.easymap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
class PoiServiceImplTest {

    private final PoiServiceImpl poiService;

    private final UserRepository userRepository;

    private final DetailedCategoryRepository detailedCategoryRepository;

    private final TagRepository tagRepository;



}