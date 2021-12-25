package edu.spirinigor.blogengine.model;

import lombok.Data;

import javax.persistence.*;

import java.util.List;


@Entity
@Table(name = "tags")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;


    @ManyToMany
    @JoinTable(name = "tag2post"
            ,joinColumns = {@JoinColumn(name = "tag_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private List<Tag> tagList;
}
