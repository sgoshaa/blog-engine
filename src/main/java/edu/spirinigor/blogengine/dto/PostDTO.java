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

    private Integer likeCount;

    private Integer dislikeCount;

    private Integer commentCount;

    private Integer viewCount;
}
