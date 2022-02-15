package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_moderator", nullable = false)
    private Short isModerator;

    @Column(name = "reg_time",nullable = false )
    private LocalDateTime regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String code;

    @Column
    private String photo;

    @OneToMany(mappedBy = "user",
              cascade = CascadeType.ALL)
    private List<Post> postList;

    @OneToMany(mappedBy = "user",
                cascade = CascadeType.ALL)
    private List<PostComment> postComments;

}
