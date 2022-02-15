package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
}
