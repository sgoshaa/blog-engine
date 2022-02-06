package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.dto.UserDTO;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getListPost(Integer offset, Integer limit, String mode){

        List<Post> postList = postRepository.findAll();
        List<PostDTO>posts = new ArrayList<>();
        postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postList.size());
        postResponse.setPosts(posts);

        return postResponse;
    }
}
