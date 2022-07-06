package edu.spirinigor.blogengine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDto {
    private Boolean result;
    private ErrorImageDto errors;
}
