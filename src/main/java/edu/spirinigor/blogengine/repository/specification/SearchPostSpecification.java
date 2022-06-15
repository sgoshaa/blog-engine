package edu.spirinigor.blogengine.repository.specification;

import edu.spirinigor.blogengine.model.Post;
import edu.spirinigor.blogengine.model.enums.ModerationStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Component
public class SearchPostSpecification {

    public Specification<Post> getSpecification(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));
            predicates.add(criteriaBuilder.equal(root.get("moderationStatus"), ModerationStatus.ACCEPTED));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("time"), LocalDateTime.now()));
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("title")), "%" + query.toLowerCase() + "%")
                            , criteriaBuilder.like(root.get("text"), "%" + query.toLowerCase() + "%")
                    )
            );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
