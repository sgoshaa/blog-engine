package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    //   popular - сортировать по убыванию количества комментариев (посты без комментариев выводить)
    @Query("SELECT p FROM Post p LEFT JOIN  PostComment pc ON p.id = pc.post " +
            "GROUP BY  p.id ORDER BY count(pc.text) DESC")
    Page<Post> findPopularPost(Pageable pageable);

    //   best - сортировать по убыванию количества лайков (посты без лайков и дизлайков выводить)
    @Query("SELECT p  FROM Post p LEFT JOIN PostVotes pv on p.id = pv.post " +
            "GROUP BY p.id ORDER BY count(pv.value) DESC")
    Page<Post> findBestPost(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE YEAR(p.time) IN (:years) AND p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= NOW()")
    List<Post> getCalendarByYear(Integer years);

    @Query("SELECT DISTINCT YEAR(p.time) as year FROM Post p WHERE p.moderationStatus = 'ACCEPTED'" +
            " AND p.isActive = 1 AND p.time <= NOW()" +
            "ORDER BY year")
    List<Integer> getCalendar();

    @Query("SELECT p FROM Post p " +
            "WHERE date(p.time) = :date " +
            "AND p.moderationStatus = 'ACCEPTED'  AND p.isActive = 1 AND p.time <= NOW()")
    Page<Post> getPostByDate(Date date, Pageable pageable);

    @Query("select p from Post p " +
            "inner join TagToPost t2p on p.id = t2p.postId " +
            "inner join Tag t on t2p.tagId = t.id " +
            "WHERE t.name LIKE :query and moderation_status = 'ACCEPTED' and p.isActive = 1 and p.time <= NOW()")
    List<Post> getPostByTagName(String query);

    @Query("select p from Post p " +
            "inner join TagToPost t2p on p.id = t2p.postId " +
            "inner join Tag t on t2p.tagId = t.id " +
            "WHERE t.name = :query and moderation_status = 'ACCEPTED' and p.isActive = 1 and p.time <= NOW()")
    Page<Post> getPostByTagName(String query, Pageable pageable);

    @Query("select p from Post p " +
            "left join fetch p.postComments where p.id = :id and moderation_status = 'ACCEPTED' and p.time <= NOW()")
    Post getPostById(Integer id);

    @Query("select p from Post p where p.isActive = 0 and p.user.id = :userId")
    Page<Post> findAllMyByStatusInactive(Integer userId, Pageable pageable);

    @Query("select p from Post p where p.user.id = :userId and p.isActive = 1 and p.moderationStatus = :status")
    Page<Post> findAllMyByStatus(Integer userId, ModerationStatus status, Pageable pageable);

    @Query("select p from Post p where p.moderator.id = :moderatorId and p.isActive = 1 " +
            "and p.moderationStatus = :status")
    Page<Post> findAllForModeration(Integer moderatorId, ModerationStatus status, Pageable pageable);
}
