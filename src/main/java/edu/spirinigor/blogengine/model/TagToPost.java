package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
@Data
public class TagToPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   @Column(name = "post_id",nullable = false)
    private Integer postId;


    @Column(name = "tag_id",nullable = false)
    private Integer tagId;
}
