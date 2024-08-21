package com.easymap.easymap.dto.request.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequestDTO {


    @Min(0)
    @Max(10)
    @NotNull
    private Integer score;

    @NotBlank
    @JsonProperty("review_text")
    private String reviewText;

    private List<ReviewImgRequestDTO> images;
}
