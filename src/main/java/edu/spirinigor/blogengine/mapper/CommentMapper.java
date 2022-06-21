package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.CommentDto;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = DateConverter.class)
public interface CommentMapper {
    @Mapping(target = "timeStamp", source = "time", qualifiedByName = "convertDateToLong")
    CommentDto toCommentDto(PostComment postComment);
}
