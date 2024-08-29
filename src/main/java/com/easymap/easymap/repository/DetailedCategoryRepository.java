package com.easymap.easymap.repository;

import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.entity.category.DetailedCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailedCategoryRepository extends JpaRepository<DetailedCategory, Long> {


    List<DetailedCategory> findDetailedCategoryByCategory_CategoryId(Long categoryId);
}
