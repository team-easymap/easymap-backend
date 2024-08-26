package com.easymap.easymap.dto.response.category;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.user.UserAdditionalInfoResponseDto;
import com.easymap.easymap.entity.category.Category;
import com.easymap.easymap.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long categoryId;

    private String categoryName;

    private List<DetailedCategoryResponseDTO> detailedCategoryList;

    private List<TagResponseDTO> tagResponseDTOList;



}
