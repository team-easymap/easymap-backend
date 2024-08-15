package com.easymap.easymap.dto.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDTO {

    @JsonProperty("tag_id")
    private Long tagId;
}
