package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.PostResponse;
import edu.spirinigor.blogengine.dto.PostDTO;
import edu.spirinigor.blogengine.mapper.PostMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.specification.PostSpecification;
import edu.spirinigor.blogengine.util.Pagination;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final Pagination pagination;
    private final PostSpecification postSpecification;

    public PostService(PostRepository postRepository, Pagination pagination, PostSpecification postSpecification) {
        this.postRepository = postRepository;
        this.pagination = pagination;
        this.postSpecification = postSpecification;
    }

    public PostResponse getListPost(Integer offset, Integer limit, String mode) {
        Page<Post> postList;
        List<PostDTO> posts = new ArrayList<>();
        switch (mode) {
            case "popular":
                postList = postRepository.findPopularPost(pagination.getPage(offset, limit));
                postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
                break;
            case "best":
                postList = postRepository.findBestPost(pagination.getPage(offset, limit));
                postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
                break;
            case "early"://early - сортировать по дате публикации, выводить сначала старые
                postList = postRepository.findAll(pagination.getPage(offset, limit, Sort.by("time").ascending()));
                postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
                break;
            default://recent - сортировать по дате публикации, выводить сначала новые (если mode не задан
                // , использовать это значение по умолчанию)
                postList = postRepository.findAll(pagination.getPage(offset, limit, Sort.by("time").descending()));
                postList.forEach(post -> posts.add(postMapper.postToPostDTO(post)));
                break;
        }

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postList.getTotalElements());
        postResponse.setPosts(posts);

        return postResponse;
    }

    public PostResponse searchPost(Integer offset, Integer limit, String query) {

        if (query.isEmpty()){
            return getListPost(offset,limit,"recent");
        }

        Page<Post> all =
                postRepository.findAll(postSpecification.getSpecification(query),pagination.getPage(offset, limit));
        PostResponse postResponse = new PostResponse();
        if (all.getTotalElements() == 0){
            postResponse.setCount(0L);
            postResponse.setPosts(new ArrayList<>());
            return postResponse;
        }

        postResponse.setPosts(postMapper.postToListDto(all));
        postResponse.setCount(all.getTotalElements());
        return postResponse;
    }
}
