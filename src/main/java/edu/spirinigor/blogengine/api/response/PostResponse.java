package edu.spirinigor.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.spirinigor.blogengine.dto.CommentDto;
import edu.spirinigor.blogengine.dto.TagForListDto;
import edu.spirinigor.blogengine.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private Integer id;
    @JsonProperty("timestamp")
    private Long timeStamp;
    private Boolean active;
    private UserDTO user;
    private String title;
    private String text;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private List<CommentDto> comments;
    private List<String> tags;
}
