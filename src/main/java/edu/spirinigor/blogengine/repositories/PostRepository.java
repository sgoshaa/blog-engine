package edu.spirinigor.blogengine.repositories;

import edu.spirinigor.blogengine.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
