package com.easymap.easymap.entity.category;

import com.easymap.easymap.dto.response.category.CategoryResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.stream.Collectors;

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


    public static CategoryResponseDTO mapToDTO(Category category){
        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.categoryName)
                .detailedCategoryList(
                        category.getDetailedCategoryList().stream()
                                .map(DetailedCategory::mapToDTO)
                                .collect(Collectors.toList()))
                .tagResponseDTOList(
                        category.getTagList().stream()
                                .map(Tag::mapToDTO)
                                .collect(Collectors.toList()))
                .build();

    }


}
