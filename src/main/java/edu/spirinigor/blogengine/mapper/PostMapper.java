package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(uses = DateConverter.class)
public interface PostMapper {

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "timeStamp", source = "post.time", qualifiedByName = "convertDateToLong")
    @Mapping(target = "announce", expression = "java(post.getText().substring(0," +
            " Math.min(post.getText().length(), 150)))")
    @Mapping(target = "likeCount", expression = "java(post.getPostVotes()" +
            ".stream().filter(postVotes -> postVotes.getValue().equals((short)1)).count())")
    @Mapping(target = "dislikeCount", expression = "java(post.getPostVotes().stream()" +
            ".filter(postVotes -> postVotes.getValue().equals((short)-1)).count())")
    @Mapping(target = "commentCount", expression = "java(post.getPostComments().size())")
    PostDTO postToPostDTO(Post post);

    List<PostDTO> postToListDto(Page<Post> posts);
}
