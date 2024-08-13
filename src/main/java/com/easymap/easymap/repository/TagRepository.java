package com.easymap.easymap.repository;

import com.easymap.easymap.entity.category.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
