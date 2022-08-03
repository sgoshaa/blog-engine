package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {
}
