package com.easymap.easymap.dto.response.review;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.TestResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class ReviewGetResponseDTO extends ResponseDto {

    private List<ReviewResponseDTO> reviewList;

    public ReviewGetResponseDTO(List<ReviewResponseDTO> reviews) {
        super();
        this.reviewList = reviews;
    }

    public static ResponseEntity<ResponseDto> success(List<ReviewResponseDTO> reviews) {
        ResponseDto responseBody = new ReviewGetResponseDTO(reviews);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
