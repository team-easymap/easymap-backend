package com.easymap.easymap.entity.category;

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
}