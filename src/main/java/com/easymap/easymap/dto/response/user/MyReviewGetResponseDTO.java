package com.easymap.easymap.dto.response.user;

import com.easymap.easymap.dto.response.ResponseDto;
import com.easymap.easymap.dto.response.review.ReviewResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class MyReviewGetResponseDTO extends ResponseDto {

    private List<ReviewResponseDTO> reviews;

    public MyReviewGetResponseDTO(List<ReviewResponseDTO> reviews) {
        super();
        this.reviews = reviews;
    }

    public static ResponseEntity<ResponseDto> success(List<ReviewResponseDTO> reviews) {
        ResponseDto responseBody = new MyReviewGetResponseDTO(reviews);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
