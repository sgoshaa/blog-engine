package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.api.request.CreatePostRequest;
import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.Tag;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.springframework.data.domain.Page;

import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {DateConverter.class, TagMapper.class, CommentMapper.class})
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

    @Mapping(target = "comments", source = "postComments")
    @Mapping(target = "active", expression = "java(post.getIsActive() == 1)")
    @Mapping(target = "timeStamp", source = "time", qualifiedByName = "convertDateToLong")
    PostResponse postToPostResponse(Post post);

    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "moderationStatus", expression = "java(setModerationStatus())")
    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "time", expression = "java(checkTimestamp(request.getTimestamp()))")
    @Mapping(target = "viewCount", expression = "java(0)")
    Post toPost(CreatePostRequest request);

    @Mapping(target = "id", source = "currentPost.id")
    @Mapping(target = "isActive", source = "updatedPost.isActive")
    @Mapping(target = "moderationStatus", source = "currentPost.moderationStatus")
    @Mapping(target = "moderator", source = "currentPost.moderator")
    @Mapping(target = "user", source = "currentPost.user")
    @Mapping(target = "time", source = "updatedPost.time")
    @Mapping(target = "text", source = "updatedPost.text")
    @Mapping(target = "title", source = "updatedPost.title")
    @Mapping(target = "viewCount", source = "currentPost.viewCount")
    @Mapping(target = "postComments", source = "currentPost.postComments")
    @Mapping(target = "tags", expression = "java(getOnlyUniqueTags(currentPost.getTags(),updatedPost.getTags()))")//source = "updatedPost.tags")
    @Mapping(target = "postVotes", source = "currentPost.postVotes")
    Post updatePost(Post currentPost, Post updatedPost);

    default List<Tag> getOnlyUniqueTags(List<Tag> currentTags, List<Tag> updatedTags) {
        updatedTags.addAll(currentTags);
        return updatedTags.stream().distinct().collect(Collectors.toList());
    }

    default ModerationStatus setModerationStatus() {
        return ModerationStatus.NEW;
    }

    default LocalDateTime checkTimestamp(Long timestamp) {
        DateConverter dateConverter = new DateConverter();
        long l = dateConverter.convertDate(LocalDateTime.now());
        if (timestamp < l) {
            return LocalDateTime.now();
        }
        if (timestamp > l) {
            return dateConverter.convertTimestamp(timestamp);
        }
        return null;
    }
}
