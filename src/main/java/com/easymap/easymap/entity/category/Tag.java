package com.easymap.easymap.entity.category;

import com.easymap.easymap.dto.response.category.TagResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tags")
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    private String tagName;

    private Integer tagAccessibilityPoint;

    public static TagResponseDTO mapToDTO(Tag tag){
        return TagResponseDTO.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .tagAccessibilityPoint(tag.getTagAccessibilityPoint())
                .build();
    }

}
