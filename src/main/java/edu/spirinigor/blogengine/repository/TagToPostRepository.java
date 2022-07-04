package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.TagToPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagToPostRepository extends JpaRepository<TagToPost,Integer> {

    List<TagToPost>findByPostIdAndTagIdIn(Integer postId,List<Integer>tagsId);

    void deleteByPostIdAndTagIdIn(Integer postId,List<Integer>tagsId);

}
