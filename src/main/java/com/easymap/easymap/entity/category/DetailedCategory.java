package com.easymap.easymap.entity.category;

import com.easymap.easymap.dto.response.category.DetailedCategoryResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detailed_categories")
@Entity
public class DetailedCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailedCategoryId;

    private String detailedCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public static DetailedCategoryResponseDTO mapToDTO(DetailedCategory detailedCategory){
        return DetailedCategoryResponseDTO.builder()
                .detailedCategoryId(detailedCategory.getDetailedCategoryId())
                .detailedCategoryName(detailedCategory.getDetailedCategoryName())
                .build();
    }
}
