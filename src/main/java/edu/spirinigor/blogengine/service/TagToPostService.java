package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.model.TagToPost;
import edu.spirinigor.blogengine.repository.TagToPostRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class TagToPostService {

    private final TagToPostRepository tagToPostRepository;

    public TagToPostService(TagToPostRepository tagToPostRepository) {
        this.tagToPostRepository = tagToPostRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removingTagsFromPost(Integer postId, List<Integer> tagsId) {
        List<TagToPost> byPostIdAndTagIdIn = tagToPostRepository.findByPostIdAndTagIdIn(postId, tagsId);
        tagToPostRepository.deleteAll(byPostIdAndTagIdIn);
        log.log(Level.INFO,"В таблице Tag2post удалены записи: "+byPostIdAndTagIdIn);
    }
}
