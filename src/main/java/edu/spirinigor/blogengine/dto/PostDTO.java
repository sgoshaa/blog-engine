package edu.spirinigor.blogengine.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostDTO {

    private Integer id;

    @JsonProperty("timestamp")
    private Long timeStamp;

    private UserDTO user;

    private String title;

    private String announce;

    private Long likeCount;

    private Long dislikeCount;

    private Integer commentCount;

    private Integer viewCount;
}
