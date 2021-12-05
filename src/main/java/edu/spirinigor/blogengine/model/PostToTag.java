package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@Data
public class PostToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tagId;
}
