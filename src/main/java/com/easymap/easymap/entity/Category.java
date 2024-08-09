package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Entity
public class Category {
    // 최상위 카테고리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "detailedCategoryId")
    private List<DetailedCategory> detailedCategoryList;

    @OneToMany(mappedBy = "tagId")
    private List<Tag> tagList;


}
