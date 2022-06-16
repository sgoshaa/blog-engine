package edu.spirinigor.blogengine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.util.List;


@Entity
@Table(name = "tags")
@Data
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;


    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "tag2post"
            ,joinColumns = {@JoinColumn(name = "tag_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private List<Post> posts;
}
