package edu.spirinigor.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeOrDisLikeRequest {
    @JsonProperty("post_id")
    private Integer postId;
}
