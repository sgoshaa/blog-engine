package edu.spirinigor.blogengine.api.response;

import edu.spirinigor.blogengine.dto.PostDTO;
import lombok.Data;

import java.util.List;
@Data
public class PostResponse {

    private Long count;

    private List<PostDTO> posts;

}


