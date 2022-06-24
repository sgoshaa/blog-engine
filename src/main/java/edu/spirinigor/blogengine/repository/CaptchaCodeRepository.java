package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode,Integer> {

    @Query("select cc from CaptchaCode cc where cc.time <= :time")
    List<CaptchaCode> findAllOld(LocalDateTime time);

}
