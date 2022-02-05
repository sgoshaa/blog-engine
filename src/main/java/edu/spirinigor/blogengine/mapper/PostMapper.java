package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {

    @Mapping(target = "id",source = "post.id")
//    @Mapping(target = "timeStamp",source = "post.time")
    @Mapping(target = "timeStamp" , ignore = true)
    @Mapping(target = "announce",expression ="java(post.getText().substring(0, Math.min(post.getText().length(), 150)))")
    @Mapping(target = "likeCount",expression = "java(post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals(1)).count())")
    @Mapping(target = "dislikeCount",expression = "java(post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals(-1)).count())")
    @Mapping(target = "commentCount",expression = "java(post.getPostComments().size())")
    PostDTO postToPostDTO(Post post);
}
