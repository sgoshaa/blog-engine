package edu.spirinigor.blogengine.service;


import edu.spirinigor.blogengine.api.response.TagResponse;
import edu.spirinigor.blogengine.dto.TagDTO;
import edu.spirinigor.blogengine.mapper.TagMapper;
import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.Tag;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import edu.spirinigor.blogengine.repository.PostRepository;
import edu.spirinigor.blogengine.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public TagResponse getListTag(String query) {
        List<Post> posts;
//        query = "Ja";//вписал жестко чтобы проверить работу метода
        if (query.isEmpty()) {
            posts = postRepository.findAllByTimeLessThanEqualAndIsActiveAndModerationStatus(
                    LocalDateTime.now(), (short) 1, ModerationStatus.ACCEPTED);
        } else {
            posts = postRepository.getPostByTagName(query + "%");
            String finalQuery = query;//удалить если не нужно будет,когда разберусь откуда приходит query
            List<TagDTO> tags = getResult(posts).stream()
                    .filter(tagDTO -> tagDTO.getName().contains(finalQuery)).collect(Collectors.toList());
            return new TagResponse(tags);
        }
        return new TagResponse(getResult(posts));
    }

    public List<Tag> getExistingTagsOrCreateNew(List<String> tagNames) {
        Set<Tag> allByNameIn = tagRepository.findAllByNameIn(tagNames);
        if (allByNameIn.size() == 0) {
            return tagMapper.toListTag(tagNames);
        }
        List<Tag> result = new ArrayList();

        tagNames.forEach(s -> {
            Optional<Tag> optionalTag = allByNameIn.stream().filter(tag -> tag.getName().equals(s)).findFirst();
            if (optionalTag.isPresent()){
                result.add(optionalTag.get());
            }else {
                result.add(tagMapper.map(s));
            }
        });

        return result;
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

