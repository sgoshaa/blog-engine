package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.api.request.PostCommentRequest;
import edu.spirinigor.blogengine.dto.CommentDto;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.PostComment;
import edu.spirinigor.blogengine.model.User;
import edu.spirinigor.blogengine.util.UserUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(uses = DateConverter.class)
public interface CommentMapper {
    @Mapping(target = "timeStamp", source = "time", qualifiedByName = "convertDateToLong")
    CommentDto toCommentDto(PostComment postComment);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(getCurrentUser())")
    @Mapping(target = "time", expression = "java(getCurrentDate())")
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "parent", source = "parentPostComment")
    @Mapping(target = "post", source = "post")
    PostComment toPostComment(PostCommentRequest request, PostComment parentPostComment, Post post);

    default LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    default User getCurrentUser() {
        return UserUtils.getCurrentUser();
    }
}
