package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag>findAllByNameLike(String query);

    Set<Tag> findAllByNameIn(List<String>tagNames);
}
