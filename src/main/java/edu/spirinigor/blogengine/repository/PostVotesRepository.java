package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
    Optional<PostVotes> findByPostIdAndUserId(Integer postId, Integer userId);
}
