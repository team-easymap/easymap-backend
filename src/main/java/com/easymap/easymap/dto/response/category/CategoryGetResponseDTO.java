package com.easymap.easymap.dto.response.category;

import com.easymap.easymap.dto.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryGetResponseDTO extends ResponseDto{

    private List<CategoryResponseDTO> categoryResponseDTOList;

    public CategoryGetResponseDTO(List<CategoryResponseDTO> categoryResponseDTOList) {
        super();
        this.categoryResponseDTOList = categoryResponseDTOList;
    }
    public static ResponseEntity<ResponseDto> success(List<CategoryResponseDTO> categoryResponseDTOList) {
        ResponseDto responseBody = new CategoryGetResponseDTO(categoryResponseDTOList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
