package edu.spirinigor.blogengine.mapper;

import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.dto.UserDTO;
import edu.spirinigor.blogengine.mapper.converter.DateConverter;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.data.domain.Page;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-14T17:08:03+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14.1 (Amazon.com Inc.)"
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
        postDTO.setLikeCount( post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals((short)1)).count() );
        postDTO.setDislikeCount( post.getPostVotes().stream().filter(postVotes -> postVotes.getValue().equals((short)-1)).count() );
        postDTO.setCommentCount( post.getPostComments().size() );

        return postDTO;
    }

    @Override
    public List<PostDTO> postToListDto(Page<Post> posts) {
        if ( posts == null ) {
            return null;
        }

        List<PostDTO> list = new ArrayList<PostDTO>();
        for ( Post post : posts ) {
            list.add( postToPostDTO( post ) );
        }

        return list;
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
