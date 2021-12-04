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

    @Column(name = "is_moderator")
    private Short isModerator;

    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String code;

    @Column
    private String photo;

    @OneToMany(mappedBy = "userId",
              cascade = CascadeType.ALL)
    private List<Post> postList;

    @OneToMany(mappedBy = "userId",
                cascade = CascadeType.ALL)
    private List<PostComment> postComments;

}
