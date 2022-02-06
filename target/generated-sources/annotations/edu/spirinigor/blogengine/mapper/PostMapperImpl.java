package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.dto.UserDTO;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-02-06T16:19:31+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    private final DateConverter dateConverter = new DateConverter();

    @Override
    public PostDTO postToPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setId( post.getId() );
        postDTO.setTimeStamp( dateConverter.convertDate( post.getTime() ) );
        postDTO.setUser( userToUserDTO( post.getUser() ) );
        postDTO.setTitle( post.getTitle() );
        postDTO.setViewCount( post.getViewCount() );

        postDTO.setAnnounce( post.getText().substring(0, Math.min(post.getText().length(), 150)) );
        postDTO.setLikeCount( post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals(1)).count() );
        postDTO.setDislikeCount( post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals(-1)).count() );
        postDTO.setCommentCount( post.getPostComments().size() );

        return postDTO;
    }

    protected UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setName( user.getName() );

        return userDTO;
    }
}
