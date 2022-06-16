package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag>findAllByNameLike(String query);
}
