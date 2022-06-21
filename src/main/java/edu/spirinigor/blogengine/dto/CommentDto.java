package edu.spirinigor.blogengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDto {
    private Integer id;
    @JsonProperty("timestamp")
    private Long timeStamp;
    private String text;
    private UserForCommentDto user;
}
