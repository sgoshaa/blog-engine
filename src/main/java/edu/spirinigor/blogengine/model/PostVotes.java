package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_votes")
@Data
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private Short value;
}
