package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    User findByName(String name);

    Optional<User> findByCode(String code);
}
