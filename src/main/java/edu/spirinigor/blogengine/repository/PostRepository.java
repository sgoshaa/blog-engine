package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    //   popular - сортировать по убыванию количества комментариев (посты без комментариев выводить)
    @Query("SELECT p FROM Post p LEFT JOIN  PostComment pc ON p.id = pc.post GROUP BY  p.id ORDER BY count(pc.text) DESC")
    Page<Post> findPopularPost(Pageable pageable);

    //   best - сортировать по убыванию количества лайков (посты без лайков и дизлайков выводить)
    @Query("SELECT p  FROM Post p LEFT JOIN PostVotes pv on p.id = pv.post GROUP BY p.id ORDER BY count(pv.value) DESC")
    Page<Post> findBestPost(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE YEAR(p.time) IN (:years) AND p.isActive = 1 AND p.moderationStatus = 'ACCEPTED'")
    List<Post> getCalendarByYear(Integer years);

    @Query("SELECT DISTINCT YEAR(p.time) as year FROM Post p WHERE p.moderationStatus = 'ACCEPTED' AND p.isActive = 1 ORDER BY year")
    List<Integer> getCalendar();
}
