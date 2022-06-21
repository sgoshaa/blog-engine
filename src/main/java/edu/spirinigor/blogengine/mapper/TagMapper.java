package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.TagDTO;
import edu.spirinigor.blogengine.mapper.converter.TagConverter;
import edu.spirinigor.blogengine.model.Tag;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = TagConverter.class)
public interface TagMapper {

    List<TagDTO> toListDto(List<Tag>tags);

    @IterableMapping(qualifiedByName = "tagNameToString")
    List<String> toListTagName(List<Tag>tags);

}
