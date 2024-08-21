package com.easymap.easymap.repository;

import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.entity.category.DetailedCategory;
import com.easymap.easymap.entity.category.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Slf4j
//@SpringBootTest
//@ActiveProfiles("dev")
class CategoryRepositoryTest {

//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Transactional
//    @Test
//    public void 읽기테스트(){
//        List<Category> list = categoryRepository.findAll();
//
//        list.stream().forEach(category -> {
//            List<DetailedCategory> detailedCategoryList = category.getDetailedCategoryList();
//            List<Tag> tagList = category.getTagList();
//            detailedCategoryList.forEach(dc-> log.info("디테일카테고리명 :" +dc.getDetailedCategoryName()));
//            log.info("-----------------------");
//            tagList.forEach(tag-> log.info("태그명 :" + tag.getTagName()));
//            log.info("=========================");
//        });
//
//    }




}