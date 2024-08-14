package com.easymap.easymap.entity.category;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@ToString
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

    @OneToMany(mappedBy = "category")
    @BatchSize(size = 10)
    private List<DetailedCategory> detailedCategoryList;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "category")
    private List<Tag> tagList;


}
