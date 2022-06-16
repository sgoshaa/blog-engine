package edu.spirinigor.blogengine.service;


import edu.spirinigor.blogengine.api.response.TagResponse;
import edu.spirinigor.blogengine.dto.TagDTO;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public TagResponse getListTag(String query) {
        List<Post> posts;
       // query = "Ja";//вписал жестко чтобы проверить работу метода
        if (query.isEmpty()) {
            posts = postRepository.findAll();
        } else {
            posts = postRepository.getPostByTagName(query + "%");
        }
        return new TagResponse(getResult(posts));
    }

    private List<TagDTO> getResult(List<Post> posts) {
        List<TagDTO> tags = new ArrayList<>();
        double countPosts = posts.size();
        double dWeightMax = 0.0;
        double countPopularTag = posts.stream()
                .filter(post -> post.getTags().size() != 0)
                .flatMap(post -> post.getTags().stream())
                .distinct()
                .map(tag1 -> tag1.getPosts().size())
                .max((o1, o2) -> o1 - o2)
                .get();

        dWeightMax = (countPopularTag / countPosts);

        double k = 1 / dWeightMax;

        posts.stream()
                .filter(post -> post.getTags().size() != 0)
                .flatMap(post -> post.getTags().stream())
                .distinct()
                .forEach(tag ->
                        tags.add(new TagDTO(tag.getName(), (double) (tag.getPosts().size() / countPosts) * k))
                );
        return tags;
    }
}

