package edu.spirinigor.blogengine.api.response;

import edu.spirinigor.blogengine.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private List<TagDTO> listTag;
}
