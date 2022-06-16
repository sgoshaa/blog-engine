package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.TagDTO;
import edu.spirinigor.blogengine.model.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    List<TagDTO> toListDto(List<Tag>tags);

}
