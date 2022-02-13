package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.dto.UserDTO;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getListPost(Integer offset, Integer limit, String mode){
        Page<Post> postList = null;
        List<PostDTO>posts = new ArrayList<>();

        if (mode.equals("recent")|| mode.equals("")){
            int page = offset/limit;
            postList = postRepository.findAll(PageRequest.of(page, limit, Sort.by("time").descending()));
            postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
        }
        if (mode.equalsIgnoreCase("popular")){
            int page = offset/limit;
            postList = postRepository.findAll(PageRequest.of(page, limit, Sort.by("time").descending()));
            postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
        }


        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postList.getTotalElements());
        postResponse.setPosts(posts);

        return postResponse;
    }
}
