package com.easymap.easymap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "detailed_categorys")
@Entity
public class DetailedCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailedCategoryId;

    private String detailedCategoryName;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
